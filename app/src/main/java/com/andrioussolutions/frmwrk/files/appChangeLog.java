package com.andrioussolutions.frmwrk.files;

import com.andrioussolutions.files.ViewLog;
import com.gtfp.errorhandler.ErrorHandler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * Copyright (C) 2011-2013, Karsten Priegnitz
 *
 * Permission to use, copy, modify, and distribute this piece of software
 * for any purpose with or without fee is hereby granted, provided that
 * the above copyright notice and this permission notice appear in the
 * source code of all copies.
 *
 * It would be appreciated if you mention the author in your change log,
 * contributors list or the like.
 *
 * @author: Karsten Priegnitz
 * @see: http://code.google.com/p/android-change-log/
 * @author Modified by: Greg Perry     2017
 *
 *
 * In MainActivity.java:
 *
 * ChangeLog cl = new ChangeLog(this);
 *
 * if (cl.firstRun())
 * cl.getLogDialog().show();
 *
 * // Display the full change log
 * cl.getFullLogDialog().show();
 *
 *
 * Abstract explanation:
 *
 * First input HTML as if writing a web page until the body-Tag. Here you can enter CSS for
 * div.title, div.subtitle, div.freetext and div.list (see below) and any other elements you may be
 * using, e.g. links.
 * To begin a section for any version start the line with a $ sign followed by the version. This
 * line will not be displayed in the dialog but it is important for Android Change Log for knowing
 * what to display and what not. It corresponds to the versionName in the AndroidManifest.xml.
 * % starts a line of a version section title.
 * _ starts a line of a version section subtitle.
 * ! starts a line of free text.
 * # starts a line within a numbered list.
 * starts a line within a bullet list.
 *
 * Lines starting without any of these signs (for example the HTML at the beginning of
 * changelog.txt) will be used as they are.
 * You can use HTML anywhere you want.
 * When you want to display a percentage sign in the change log, please use %25 instead of only %.
 * This is due to limitations in the use of a WebView in the dialog.
 *
 * Insert a line containing “$ END_OF_CHANGE_LOG” after the last version section.
 * After that you should enter HTML again, at least the end-body-tag and the end-html-tag.
 * You can indent lines, but you don’t have to.
 * You don’t need to use these special symbols, you can write your log completely in HTML. Only the
 * lines beginning with $-signs are mandatory if you want to be able to only display what’s new
 * instead of a full change log.
 */

public class appChangeLog{

    private static final String EOCL = "END_OF_CHANGE_LOG";

    private Listmode listMode = Listmode.NONE;

    private WebView mWebView;

    private ViewLog mLogView;

    private Resources mResource;

    private int mLogFile;

    private String mFileName;

    private StringBuffer sb = null;



    public appChangeLog(Context context, @RawRes int logFile){

        this(context);

        mLogFile = logFile;
    }



    public appChangeLog(Context context, String logFile){

        this(context);

        mFileName = logFile;
    }



    public appChangeLog(Context context){

        mLogView = new ViewLog(context);

        mWebView = new WebView(context);

        mWebView.setBackgroundColor(Color.parseColor("black"));

        mResource = context.getResources();
    }



    public appChangeLog setFile(@RawRes int logFile){

        if (logFile <= 0){ return this; }

        mLogFile = logFile;

        mFileName = null;

        return this;
    }




    public appChangeLog setFile(String fileName){

        if (fileName == null || fileName.isEmpty()){ return this; }

        mFileName = fileName.trim();

        mLogFile = 0;

        return this;
    }




    public appChangeLog setTitle(@StringRes int title){

        mLogView.setTitle(title);

        return this;
    }




    public appChangeLog setTitle(String title){

        mLogView.setTitle(title);

        return this;
    }




    public void show(){

        mWebView.loadDataWithBaseURL(null, this.getLog(true), "text/html", "UTF-8", null);

        mLogView
                .setView(mWebView)
                .show();
    }



    protected String getLog(boolean full){

        if (mLogFile == 0 && mFileName == null){ return ""; }

        InputStream ins;

        ins = mResource.openRawResource(mLogFile);
//        }else{
//
//            ins = appFile.FileInputStream(mFileName);
//        }

        if (ins == null){ return ""; }

        BufferedReader br = new BufferedReader(new InputStreamReader(ins));

        String line;

        boolean advanceToEOVS = false; // if true: ignore further version sections

        sb = new StringBuffer();

        try{

            while ((line = br.readLine()) != null){

                line = line.trim();

                 logFormat(line, full, advanceToEOVS);
            }

             this.closeList();

             br.close();

        }catch (Exception ex){

            ErrorHandler.logError(ex);
        }

        return sb.toString();
    }



    private void logFormat(String line, boolean full, boolean advanceToEOVS){

        char marker = line.length() > 0 ? line.charAt(0) : 0;

        if (marker == '$'){

            // begin of a version section
            this.closeList();

            String version = line.substring(1).trim();

            // stop output?
            if (!full){

                if (version.equals(EOCL)){

                    advanceToEOVS = false;
                }else{

                    advanceToEOVS = true;
                }
            }
        }else if (!advanceToEOVS){

            switch (marker){
                case '%':
                    // line contains version title
                    this.closeList();

                    sb.append("<div class='title'>").append(line.substring(1).trim())
                            .append("</div>\n");

                    break;

                case '_':

                    // line contains version title
                    this.closeList();

                    sb.append("<div class='subtitle'>").append(line.substring(1).trim())
                            .append("</div>\n");

                    break;

                case '!':
                    // line contains free text
                    this.closeList();

                    sb.append("<div class='freetext'>").append(line.substring(1).trim())
                            .append("</div>\n");

                    break;

                case '#':
                    // line contains numbered list item
                    this.openList(Listmode.ORDERED);

                    sb.append("<li>").append(line.substring(1).trim()).append("</li>\n");

                    break;

                case '*':
                    // line contains bullet list item
                    this.openList(Listmode.UNORDERED);

                    sb.append("<li>").append(line.substring(1).trim()).append("</li>\n");

                    break;

                default:
                    // no special character: just use line as is
                    this.closeList();

                    sb.append(line).append("\n");
            }
        }
    }



    protected void openList(Listmode listMode){

        if (this.listMode != listMode){

            closeList();

            if (listMode == Listmode.ORDERED){

                sb.append("<div class='list'><ol>\n");

            }else if (listMode == Listmode.UNORDERED){

                sb.append("<div class='list'><ul>\n");
            }

            this.listMode = listMode;
        }
    }



    protected void closeList(){

        if (this.listMode == Listmode.ORDERED){

            sb.append("</ol></div>\n");

        }else if (this.listMode == Listmode.UNORDERED){

            sb.append("</ul></div>\n");
        }

        this.listMode = Listmode.NONE;
    }



    /** modes for HTML-Lists (bullet, numbered) */
    private enum Listmode{
        NONE, ORDERED, UNORDERED,
    }
}
