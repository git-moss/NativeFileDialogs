// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.nativefiledialogs.macos;

import de.mossgrabers.nativefiledialogs.FileFilter;
import de.mossgrabers.nativefiledialogs.NativeFileDialogs;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


/**
 * The Macos implementation for the file dialogs.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NativeMacosFileDialogs implements NativeFileDialogs
{
    /**
     * Creates a new file dialog instance with the initial directory.
     *
     * @param currentDirectory The initial directory, may be null
     */
    public NativeMacosFileDialogs (final File currentDirectory)
    {
        // TODO Auto-generated constructor stub
    }


    /** {@inheritDoc} */
    @Override
    public File selectFile (final FileFilter... filters)
    {
      //  String applescriptCommand = "say \"Hello from Java\"\\n";
        
        Runtime runtime = Runtime.getRuntime();
        
        // an applescript command that is multiple lines long.
        // just create a java string, and remember the newline characters.
//        String applescriptCommand =  "tell app \"iTunes\"\n" + 
//                                       "activate\n" +
//                                       "set sound volume to 40\n" + 
//                                       "set EQ enabled to true\n" +
//                                       "play\n" +
//                                     "end tell";
        
        String applescriptCommand = "tell application \"Finder\"\n" + 
        "Activate\n"+
        "try\n" +
        "POSIX path of ( choose file name " +
//        if ( aTitle && strlen(aTitle) )
//        {
//            strcat(lDialogString, "with prompt \"") ;
//            strcat(lDialogString, aTitle) ;
//            strcat(lDialogString, "\" ") ;
//        }
//        getPathWithoutFinalSlash ( lString , aDefaultPathAndFile ) ;
//        if ( strlen(lString) )
//        {
//            strcat(lDialogString, "default location \"") ;
//            strcat(lDialogString, lString ) ;
//            strcat(lDialogString , "\" " ) ;
//        }
//        getLastName ( lString , aDefaultPathAndFile ) ;
//        if ( strlen(lString) )
//        {
//            strcat(lDialogString, "default name \"") ;
//            strcat(lDialogString, lString ) ;
//            strcat(lDialogString , "\" " ) ;
//        }
        ")\n"+
        "on error number -128\n" +
        "end try\n" +
"end tell";
        

        String[] args = { "osascript", "-e", applescriptCommand };
        
        
        /* Create the ProcessBuilder */
        ProcessBuilder pb = new ProcessBuilder(Arrays.asList (args));
        pb.redirectErrorStream(true);

        StringBuilder result = new StringBuilder ();
        
        /* Start the process */
        Process proc;
        try
        {
            proc = pb.start();

            /* Read the process's output */
            String line;             
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    proc.getInputStream()));             
            while ((line = in.readLine()) != null) {
                result.append (line);
            }

            /* Clean-up */
            proc.destroy();
        }
        catch (IOException ex)
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
            
            return null;
        }
        
        final String filename = result.toString ().trim ();
        return filename.isEmpty () ? null : new File (filename);
    }


    /** {@inheritDoc} */
    @Override
    public File selectNewFile (final FileFilter... filters)
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** {@inheritDoc} */
    @Override
    public File selectFolder (final String title)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
