// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019-2021
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs;

/**
 * Helper class for wrapping results from a process execution.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class ProcessResult
{
    private final String result;
    private final String error;
    private final int    exitCode;

    /**
     * Constructor.
     *
     * @param result The result text, if any (never null)
     * @param error The error text, if any (never null)
     * @param exitCode The exit code
     */
    public ProcessResult (final String result, final String error, final int exitCode)
    {
        this.result = result;
        this.error = error;
        this.exitCode = exitCode;
    }


    /**
     * Get the result text.
     *
     * @return The result text
     */
    public String getResult ()
    {
        return this.result;
    }


    /**
     * Get the error text.
     *
     * @return The error text
     */
    public String getError ()
    {
        return this.error;
    }


    /**
     * Get the exit code.
     *
     * @return The exit code
     */
    public int getExitCode ()
    {
        return this.exitCode;
    }
}
