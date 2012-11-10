/*
 * Copyright (C) 2012 by Stefan Rothe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY); without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.platform;

public class LogInfo {

    private String buttonText;
    private String log;
    private String title;

    public String getButtonText() {
        return this.buttonText;
    }

    public String getLog() {
        return this.log;
    }

    public String getTitle() {
        return this.title;
    }

    public void setButtonText(String value) {
        this.buttonText = value;
    }

    public void setLog(String value) {
        this.log = value;
    }

    public void setTitle(String value) {
        this.title = value;
    }
}
