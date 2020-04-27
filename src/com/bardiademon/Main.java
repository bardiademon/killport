package com.bardiademon;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main
{
    private String[] split;
    private String port;

    private BufferedReader readerResult;
    private Process exec;
    private String lineResult, lineYN;
    private String pid;
    private ProcessBuilder taskkill;

    private Main ()

    {
        BufferedReader reader = new BufferedReader (new InputStreamReader (System.in));
        ProcessBuilder processBuilder;
        while (true)
        {
            try
            {
                System.out.print ("Port (to exit type \":exit\"): ");
                port = reader.readLine ();
                if (port.equals (":exit")) System.exit (0);

                if (port.matches ("[0-9]*"))
                {
                    processBuilder = new ProcessBuilder ("netstat.exe" , "-noa" , "-p" , "tcp");
                    exec = processBuilder.start ();
                    readerResult = new BufferedReader (new InputStreamReader (exec.getInputStream ()));
                    readerResult.lines ().forEach (line ->
                    {
                        try
                        {
                            split = line.trim ().split ("\\s+");
                            if (split[1].trim ().split (":")[1].trim ().equals (port))
                            {
                                System.out.println (line);
                                pid = split[split.length - 1].trim ();
                                System.out.print (String.format ("Kill %s (y/n): " , pid));
                                lineYN = reader.readLine ();
                                if (lineYN.trim ().toLowerCase ().equals ("y"))
                                {
                                    taskkill = new ProcessBuilder ("taskkill.exe" , "/PID" , pid , "/F");
                                    exec = taskkill.start ();
                                    exec.waitFor ();
                                    readerResult = new BufferedReader (new InputStreamReader (exec.getInputStream ()));
                                    lineResult = readerResult.readLine ();
                                    if ((((((lineResult.trim ()).split (":"))[0]).trim ()).toLowerCase ()).equals ("success"))
                                        System.out.println ("Closed " + port);
                                    exec.destroy ();
                                }
                                else throw new Exception (String.format ("%s not found" , port));

                            }
                        }
                        catch (Exception ignored)
                        {
                        }
                    });
                }
                else throw new Exception ("Just number");
            }
            catch (Exception ignored)
            {
            }
        }
    }


    public static void main (String[] args)
    {
        new Main ();
    }
}
