package com.yenthefromghent.jlsp.core.lsp.types;

public class Capabilities {

    /**
     * Class that defines all the capabilities the lsp supports
     */

    public Boolean supportsConfigurationDoneRequest;

    public Boolean supportsFunctionBreakpoints;

    public Boolean supportsConditionalBreakpoints;

    public Boolean supportsHitConditionalBreakpoints;

    public Boolean supportsEvaluateForHovers;

    public Boolean supportsStepBack;

    public Boolean supportsSetVariable;

    public Boolean supportsRestartFrame;

    public Boolean supportsGotoTargetsRequest;

    public Boolean supportsStepInTargetsRequest;

    public Boolean supportsCompletionsRequest;

    public Boolean supportsModulesRequest;

    public String[] supportedChecksumAlgorithms;

    public Boolean supportsRestartRequest;

    public Boolean supportsExceptionOptions;

    public Boolean supportsValueFormattingOptions;

    public Boolean supportsExceptionInfoRequest;

    public Boolean supportTerminateDebuggee;

    public Boolean supportsDelayedStackTraceLoading;

    public Boolean supportsLoadedSourcesRequest;

    public Boolean supportsLogPoints;

    public Boolean supportsTerminateThreadsRequest;

    public Boolean supportsSetExpression;

    public Boolean supportsTerminateRequest;

    public Boolean supportsDataBreakpoints;

    public Boolean supportsReadMemoryRequest;

    public Boolean supportsDisassembleRequest;

}
