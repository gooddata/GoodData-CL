package com.gooddata.processor;

import com.gooddata.exception.ProcessingException;

/**
 * GoodData
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public interface Executor {


    /**
     * Processes single command
     * @param c command to be processed
     * @param cli parameters (commandline params)
     * @param ctx processing context
     * @return true if the command has been processed, false otherwise
     */
    public boolean processCommand(Command c, CliParams cli, ProcessingContext ctx) throws ProcessingException;

}
