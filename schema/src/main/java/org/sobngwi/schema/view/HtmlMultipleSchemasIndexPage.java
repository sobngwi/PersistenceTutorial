/*
 * This file is a part of the SchemaSpy project (http://schemaspy.sourceforge.net).
 * Copyright (C) 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011 John Currier
 *
 * SchemaSpy is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * SchemaSpy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.sobngwi.schema.view;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.sobngwi.schema.Config;
import org.sobngwi.schema.util.LineWriter;

/**
 * The page that contains links to the various schemas that were analyzed
 *
 * @author John Currier
 */
public class HtmlMultipleSchemasIndexPage extends HtmlFormatter {
    private static HtmlMultipleSchemasIndexPage instance = new HtmlMultipleSchemasIndexPage();

    /**
     * Singleton: Don't allow instantiation
     */
    private HtmlMultipleSchemasIndexPage() {
    }

    /**
     * Singleton accessor
     *
     * @return the singleton instance
     */
    public static HtmlMultipleSchemasIndexPage getInstance() {
        return instance;
    }

    public void write(String dbName, List<String> populatedSchemas, DatabaseMetaData meta, LineWriter index) throws IOException {
        writeHeader(dbName, meta, populatedSchemas.size(), false, populatedSchemas.get(0).toString(), index);

        for (String schema : populatedSchemas) {
            writeLineItem(schema, index);
        }

        writeFooter(index);
    }

    private void writeHeader(String databaseName, DatabaseMetaData meta, int numberOfSchemas, boolean showIds, String aSchema, LineWriter html) throws IOException {
        String connectTime = new SimpleDateFormat("EEE MMM dd HH:mm z yyyy").format(new Date());

        html.writeln("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>");
        html.writeln("<html>");
        html.writeln("<head>");
        html.write("  <title>SchemaSpy Analysis");
        if (databaseName != null) {
            html.write(" of Database ");
            html.write(databaseName);
        }
        html.writeln("</title>");
        html.write("  <link rel=stylesheet href='");
        html.write(aSchema);
        html.writeln("/schemaSpy.css' type='text/css'>");
        html.writeln("  <meta HTTP-EQUIV='Content-Type' CONTENT='text/html; charset=" + Config.getInstance().getCharset() + "'>");
        html.writeln("</head>");
        html.writeln("<body>");
        writeTableOfContents(html);
        html.writeln("<div class='content' style='clear:both;'>");
        html.writeln("<table width='100%' border='0' cellpadding='0'>");
        html.writeln(" <tr>");
        html.write("  <td class='heading' valign='top'><h1>");
        html.write("SchemaSpy Analysis");
        if (databaseName != null) {
            html.write(" of Database ");
            html.write(databaseName);
        }
        html.writeln("</h1></td>");
        html.writeln("  <td class='heading' align='right' valign='top' title='Alain SOBNGWI - Creator of Cool Tools'><span class='indent'></span><br><span class='indent'><span class='signature'><a href='http://www.sobngwi.org' target='_blank'></a></span></span></td>");
        html.writeln(" </tr>");
        html.writeln("</table>");
        html.writeln("<table width='100%'>");
        html.writeln(" <tr><td class='container'>");
        writeGeneratedOn(connectTime, html);
        html.writeln(" </td></tr>");
        html.writeln(" <tr>");
        html.write("  <td class='container'>");
        if (meta != null) {
            html.write("Database Type: ");
            html.write(getDatabaseProduct(meta));
        }
        html.writeln("  </td>");
        html.writeln("  <td class='container' align='right' valign='top' rowspan='3'>");
        if (sourceForgeLogoEnabled())
            html.writeln("    <a href='http://sourceforge.net' target='_blank'><img src='http://sourceforge.net/sflogo.php?group_id=137197&amp;type=1' alt='SourceForge.net' border='0' height='31' width='88'></a><br>");
        html.write("    <br>");
        html.writeln("  </td>");
        html.writeln(" </tr>");
        html.writeln("</table>");

        html.writeln("<div class='indent'>");
        html.write("<b>");
        html.write(String.valueOf(numberOfSchemas));
        if (databaseName != null)
            html.write(" Schema");
        else
            html.write(" Database");
        html.write(numberOfSchemas == 1 ? "" : "s");
        html.writeln(":</b>");
        html.writeln("<TABLE class='dataTable' border='1' rules='groups'>");
        html.writeln("<colgroup>");
        html.writeln("<thead align='left'>");
        html.writeln("<tr>");
        html.write("  <th valign='bottom'>");
        if (databaseName != null)
            html.write("Schema");
        else
            html.write("Database");
        html.writeln("</th>");
        if (showIds)
            html.writeln("  <th align='center' valign='bottom'>ID</th>");
        html.writeln("</tr>");
        html.writeln("</thead>");
        html.writeln("<tbody>");
    }

    private void writeLineItem(String schema, LineWriter index) throws IOException {
        index.writeln(" <tr>");
        index.write("  <td class='detail'><a href='");
        index.write(schema);
        index.write("/index.html'>");
        index.write(schema);
        index.writeln("</a></td>");
        index.writeln(" </tr>");
    }

    @Override
    protected void writeTableOfContents(LineWriter html) throws IOException {
        // have to use a table to deal with a horizontal scrollbar showing up inappropriately
        html.writeln("<table id='headerHolder' cellspacing='0' cellpadding='0'><tr><td>");
        html.writeln("<div id='header'>");
        html.writeln(" <ul>");
        html.writeln("  <li id='current'><a href='index.html' title='All user schemas in the database'>Schemas</a></li>");
        html.writeln(" </ul>");
        html.writeln("</div>");
        html.writeln("</td></tr></table>");
    }

    @Override
    protected void writeFooter(LineWriter html) throws IOException {
        html.writeln("</tbody>");
        html.writeln("</table>");
        super.writeFooter(html);
    }

    /**
     * Copy / paste from Database, but we can't use Database here...
     *
     * @param meta DatabaseMetaData
     * @return String
     */
    private String getDatabaseProduct(DatabaseMetaData meta) {
        try {
            return meta.getDatabaseProductName() + " - " + meta.getDatabaseProductVersion();
        } catch (SQLException exc) {
            return "";
        }
    }
}
