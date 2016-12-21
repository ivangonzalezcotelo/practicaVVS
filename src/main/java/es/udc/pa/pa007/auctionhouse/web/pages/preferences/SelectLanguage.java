package es.udc.pa.pa007.auctionhouse.web.pages.preferences;

import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;

import es.udc.pa.pa007.auctionhouse.web.pages.Index;
import es.udc.pa.pa007.auctionhouse.web.util.SupportedLanguages;

/**
 * SelectLanguage.
 *
 */
public class SelectLanguage {

    /**
     * The language.
     */
    @Property
    private String language;

    /**
     * The Locale.
     */
    @Inject
    private Locale locale;

    /**
     * The PersistentLocale.
     */
    @Inject
    private PersistentLocale persistentLocale;

    /**
     * onPrepareForRender.
     */
    public void onPrepareForRender() {
        language = locale.getLanguage();
    }

    /**
     * @return the languages.
     */
    public String getLanguages() {
        return SupportedLanguages.getOptions(locale.getLanguage());
    }

    /**
     * @return the Index.
     */
    public Object onSuccess() {

        persistentLocale.set(new Locale(language));

        return Index.class;

    }

}