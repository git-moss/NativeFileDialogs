// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


/**
 * Abstract implementation of the file selection dialogs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public abstract class AbstractNativeFileDialogs implements NativeFileDialogs
{
    protected File currentDirectory;


    /**
     * Constructor.
     *
     * @param currentDirectory The initial directory, may be null
     */
    protected AbstractNativeFileDialogs (final File currentDirectory)
    {
        if (currentDirectory != null)
            this.currentDirectory = currentDirectory.isDirectory () ? currentDirectory : currentDirectory.getParentFile ();
    }


    /** {@inheritDoc} */
    @Override
    public File selectFile () throws IOException
    {
        return this.selectFile ((String) null);
    }


    /** {@inheritDoc} */
    @Override
    public File selectFile (final FileFilter... filters) throws IOException
    {
        return this.selectFile ((String) null, filters);
    }


    /** {@inheritDoc} */
    @Override
    public File selectNewFile () throws IOException
    {
        return this.selectNewFile ((String) null);
    }


    /** {@inheritDoc} */
    @Override
    public File selectNewFile (final FileFilter... filters) throws IOException
    {
        return this.selectNewFile ((String) null, filters);
    }


    /** {@inheritDoc} */
    @Override
    public File selectFolder () throws IOException
    {
        return this.selectFolder ((String) null);
    }


    /**
     * Execute a command line process.
     *
     * @param args The arguments of the process.
     * @return The read output
     * @throws IOException Could not execute the process
     */
    protected static String executeProcess (final String [] args) throws IOException
    {
        final ProcessBuilder pb = new ProcessBuilder (Arrays.asList (args));

        final StringBuilder result = new StringBuilder ();

        // Start the process
        final Process proc = pb.start ();

        // Read the process's output
        try (final BufferedReader in = new BufferedReader (new InputStreamReader (proc.getInputStream ())))
        {
            String line;
            while ((line = in.readLine ()) != null)
                result.append (line);
        }
        finally
        {
            proc.destroy ();
        }

        return result.toString ().trim ();
    }
}
