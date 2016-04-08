package quality.gates.jenkins.plugin;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.Builder;
import org.kohsuke.stapler.DataBoundConstructor;


public class QGBuilder extends Builder {

    private JobConfigData jobConfigData;
    private BuildDecision buildDecision;
    private JobExecutionService jobExecutionService;

    @DataBoundConstructor
    public QGBuilder(JobConfigData jobConfigData) {
        this.jobConfigData = jobConfigData;
        this.jobExecutionService = new JobExecutionService();
        this.buildDecision = new BuildDecision();
    }

    protected QGBuilder(JobConfigData jobConfigData, BuildDecision buildDecision, JobExecutionService jobExecutionService) {
        this.jobConfigData = jobConfigData;
        this.buildDecision = buildDecision;
        this.jobExecutionService = jobExecutionService;
    }

    public JobConfigData getJobConfigData() {
        return jobConfigData;
    }

    @Override
    public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
        QGBuilderDescriptor buildDescriptor;
        try {
            buildDescriptor = jobExecutionService.getBuilderDescriptor();
            GlobalConfig globalConfig = buildDescriptor.getGlobalConfig();
            boolean hasGlobalConfigWithSameName = jobExecutionService.hasGlobalConfigDataWithSameName(jobConfigData, globalConfig);
            if(!hasGlobalConfigWithSameName && !globalConfig.getListOfGlobalConfigData().isEmpty()) {
                listener.error(JobExecutionService.GLOBAL_CONFIG_NO_LONGER_EXISTS_ERROR, jobConfigData.getGlobalConfigDataForSonarInstance().getName());
                return false;
            }
        }
        catch (QGException e){
            e.printStackTrace(listener.getLogger());
        }
        return true;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace(listener.getLogger());
        }
        boolean buildHasPassed;
        try {
            buildHasPassed = buildDecision.getStatus(jobConfigData);
            if("".equals(jobConfigData.getGlobalConfigDataForSonarInstance().getName()))
                listener.getLogger().println(JobExecutionService.DEFAULT_CONFIGURATION_WARNING);
            listener.getLogger().println("Build-Step: Quality Gates plugin build passed: " + String.valueOf(buildHasPassed).toUpperCase());
            return buildHasPassed;
        }
        catch (QGException e){
            e.printStackTrace(listener.getLogger());
        }
        return false;
    }

    @Override
    public QGBuilderDescriptor getDescriptor() {
        return (QGBuilderDescriptor) super.getDescriptor();
    }


}
