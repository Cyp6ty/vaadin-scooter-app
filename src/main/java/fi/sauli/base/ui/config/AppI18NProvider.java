package fi.sauli.base.ui.config;

import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class AppI18NProvider implements I18NProvider {

    private static final Locale FINNISH = new Locale("fi", "FI");
    private static final Locale ENGLISH = Locale.ENGLISH;

    @Override
    public List<Locale> getProvidedLocales() {
        return List.of(FINNISH, ENGLISH);
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (locale == null) {
            locale = FINNISH;
        }
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        if (!bundle.containsKey(key)) {
            return "!" +  key;
        }
        String value = bundle.getString(key);

        if (params.length > 0) {
            return MessageFormat.format(value, params);
        }
        return value;
    }
}
