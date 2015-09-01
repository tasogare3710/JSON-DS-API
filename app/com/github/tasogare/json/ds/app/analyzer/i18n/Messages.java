// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.app.analyzer.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author tasogare
 *
 */
public enum Messages {

    AppTitle("app.title"),

    AnalyzeButtonLabel("toolbar.button.analyze.label"),

    ValidateButtonLabel("toolbar.button.validate.label"),

    FullscreenButtonOn("toolbar.button.toggle.fullscreen.label.state.on"),

    FullscreenButtonOff("toolbar.button.toggle.fullscreen.label.state.off"),

    jsdsChooserTitle("chooser.file.jsonds.title"),

    jsdsChooserExtFilterJsdsDesc("chooser.file.jsonds.filter.extension.jsonds.desc"),

    jsdsChooserExtFilterAllDesc("chooser.file.jsonds.filter.extension.all.desc"),

    JsonChooserTitle("chooser.file.json.title"),

    JsonChooserExtFilterJsonDesc("chooser.file.json.filter.extension.json.desc"),

    JsonChooserExtFilterAllDesc("chooser.file.json.filter.extension.all.desc"),

    LoadServiceFailedMessage("service.load.failed.message"),

    ValidateServiceSucceededMessage("service.validate.succeeded.message"){
        @Override
        public String format(ResourceBundle resources, Object... args) throws IllegalArgumentException {
            final String message = from(resources);
            return MessageFormat.format(message, args);
        }
    },

    ValidateServiceFailedMessage("service.validate.failed.message");

    private final String key;
    private Messages(String key) {
        this.key = key;
    }

    /**
     * 
     * @param resources
     * @return
     */
    public String from(ResourceBundle resources) {
        return resources.getString(key);
    }

    /**
     * 
     * @param resources
     * @param args
     * @return
     * @throws UnsupportedOperationException
     */
    public String format(ResourceBundle resources, Object... args) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
