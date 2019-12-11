/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019 Kyle Chatman
 *
 * Based on:
 *
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.etoitau.pixeldungeon.windows;

import com.etoitau.pixeldungeon.PixelDungeon;
import com.etoitau.pixeldungeon.ui.HighlightedText;
import com.etoitau.pixeldungeon.ui.Icons;
import com.etoitau.pixeldungeon.ui.RedButton;
import com.etoitau.pixeldungeon.ui.Window;
import com.etoitau.pixeldungeon.utils.Utils;
import com.watabau.noosa.ui.Component;


public class WndError extends Window {

    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 144;
    private static final int GAP = 2;
    private static final int BTN_HEIGHT = 18;

    private static final String TXT_TITLE = "ERROR";
    private static final String TXT_FULL_REPORT = "FULL REPORT";

    public WndError(String message) {
        this(message, null);
    }

    public WndError(String message, final Exception e) {
        super();

        int width = PixelDungeon.landscape() ? WIDTH_L : WIDTH_P;
        float pos = 0;

        // title
        final Component titlebar = new IconTitle(Icons.WARNING.get(), TXT_TITLE);
        titlebar.setRect(0, 0, width, 0);
        add(titlebar);

        // short message
        HighlightedText text = new HighlightedText(6);
        text.text(message, width);
        text.setPos(titlebar.left(), titlebar.bottom() + GAP);
        add(text);

        pos = text.bottom();

        if (e != null) {
            // button to show stacktrace
            RedButton fullReptButton = new RedButton(Utils.capitalize(TXT_FULL_REPORT)) {
                @Override
                protected void onClick() {
                    add(new WndTitledMessage(new IconTitle(Icons.WARNING.get(), TXT_TITLE),
                            WndError.getformatedStackTrace(e)));
                }

            };
            fullReptButton.setRect(0, text.bottom() + GAP, (width - GAP) / 2, BTN_HEIGHT);
            add(fullReptButton);
            pos = fullReptButton.bottom();
        }


        resize(width, (int) pos);
    }

    public static String getformatedStackTrace(Throwable e) {
        StringBuilder sb = new StringBuilder();
        if (e == null) { return ""; }

        // add cause
        sb.append(stripDots(e.toString())).append("\n");

        StackTraceElement[] traceArray = e.getStackTrace();
        int linesToAdd = Math.min(8, traceArray.length);
        // add trace
        for (int i = 0; i < linesToAdd; i++) {
            sb.append(traceArray[i].getFileName()).append(" ");
            sb.append(traceArray[i].getLineNumber()).append(" ");
            sb.append(traceArray[i].getMethodName()).append("\n");
        }
        return sb.toString();
    }

    // takes string and keeps only last thing
    private static String stripDots(String input) {
        StringBuilder sb = new StringBuilder();

        String[] words = input.split(" ");
        String[] wordSplit;

        for (String word: words) {
            wordSplit = word.split("\\.");
            sb.append(wordSplit[wordSplit.length - 1]);
            sb.append(" ");
        }
        sb.setLength(sb.length() - 1);

        return sb.toString();
    }




}
