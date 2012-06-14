package org.appfuse.webapp.taglib;

import java.io.IOException;
import java.text.Collator;
import java.util.*;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.appfuse.model.LabelValue;
import org.displaytag.tags.el.ExpressionEvaluator;

/**
 * Tag for creating multiple &lt;select&gt; options for displaying a list of
 * language names.
 *
 * <p>
 * <b>NOTE</b> - This tag requires a Java2 (JDK 1.2 or later) platform.
 * </p>
 *
 * @author Jens Fischer, Matt Raible, Martin Homik
 * @version $Revision: 1.6 $ $Date: 2006/07/15 11:57:20 $
 *
 * @jsp.tag name="language" bodycontent="empty"
 */
public class LanguageTag extends TagSupport {
    
	private static final long serialVersionUID = -379264038430970213L;

	private String name;
    private String prompt;
    private String scope;
    private String selected;

    /**
     * @param name The name to set.
     *
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param prompt The prompt to set.
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * @param selected The selected option.
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setDefault(String selected) {
        this.selected = selected;
    }

    /**
     * Property used to simply stuff the list of languages into a
     * specified scope.
     *
     * @param scope
     *
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setToScope(String scope) {
        this.scope = scope;
    }

    /**
     * Process the start of this tag.
     *
     * @return int status
     *
     * @exception JspException if a JSP exception has occurred
     *
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        ExpressionEvaluator eval = new ExpressionEvaluator(this, pageContext);

        if (selected != null) {
            selected = eval.evalString("default", selected);
        }

        Locale userLocale = pageContext.getRequest().getLocale();
        List languages = this.buildLanguageList(userLocale,eval);

        if (scope != null) {
            if (scope.equals("page")) {
                pageContext.setAttribute(name, languages);
            } else if (scope.equals("request")) {
                pageContext.getRequest().setAttribute(name, languages);
            } else if (scope.equals("session")) {
                pageContext.getSession().setAttribute(name, languages);
            } else if (scope.equals("application")) {
                pageContext.getServletContext().setAttribute(name, languages);
            } else {
                throw new JspException("Attribute 'scope' must be: page, request, session or application");
            }
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("<select name=\"" + name + "\" id=\"" + name + "\" class=\"select\">\n");

            if (prompt != null) {
                sb.append("    <option value=\"\" selected=\"selected\">");
                sb.append(eval.evalString("prompt", prompt) + "</option>\n");
            }

            for (Iterator i = languages.iterator(); i.hasNext();) {
                LabelValue language = (LabelValue) i.next();
                sb.append("    <option value=\"" + language.getValue() + "\"");

                if ((selected != null) && selected.equals(language.getValue())) {
                    sb.append(" selected=\"selected\"");
                }

                sb.append(">" + language.getLabel() + "</option>\n");
            }

            sb.append("</select>");

            try {
                pageContext.getOut().write(sb.toString());
            } catch (IOException io) {
                throw new JspException(io);
            }
        }

        return super.doStartTag();
    }

    /**
     * Release aquired resources to enable tag reusage.
     *
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release() {
        super.release();
    }

    /**
     * Build a List of LabelValues for all the available languages. Uses
     * the two letter uppercase ISO name of the language as the value and the
     * localized language name as the label.
     *
     * @param locale The Locale used to localize the language names.
     * @param locale to call back messages
     *
     * @return List of LabelValues for all available languages.
     */
    protected List buildLanguageList(Locale locale, ExpressionEvaluator eval) {
        final String EMPTY = "";
        //final Locale[] available = Locale.getAvailableLocales();
        String admissibleCodes = null;
        try {
            admissibleCodes = eval.evalString("acceptedLanguages","en fr de nl es it cs ru pt eu fr-lu");
        } catch (JspException e) {
            e.printStackTrace();
            admissibleCodes = "en fr de nl es it cs ru pt eu";
        }
        StringTokenizer stok = new StringTokenizer(admissibleCodes," ", false);

        List languages = new ArrayList();

        while (stok.hasMoreTokens()) {
            final String iso =  stok.nextToken();
            String name = null;
            try {
                name = eval.evalString("acceptedLanguages.name." + iso,null);
                if(name==null && "fr-lu".equals(iso)) name= "Fran\u00e7ais (Luxembourg)";
            } catch (JspException e) {
                e.printStackTrace();
            }
            if(name==null) {
                name = new Locale(iso).getDisplayName(locale);
            }

            if (!EMPTY.equals(iso) && !EMPTY.equals(name)) {
                LabelValue language = new LabelValue(name, iso);

                if (!languages.contains(language)) {
                    languages.add(new LabelValue(name, iso));
                }
            }
        }

        Collections.sort(languages, new LabelValueComparator(locale));

        return languages;
    }

    /**
     * Class to compare LabelValues using their labels with
     * locale-sensitive behaviour.
     */
    public class LabelValueComparator implements Comparator {
        private Comparator c;

        /**
         * Creates a new LabelValueComparator object.
         *
         * @param locale The Locale used for localized String comparison.
         */
        public LabelValueComparator(Locale locale) {
            c = Collator.getInstance(locale);
        }

        /**
         * Compares the localized labels of two LabelValues.
         *
         * @param o1 The first LabelValue to compare.
         * @param o2 The second LabelValue to compare.
         *
         * @return The value returned by comparing the localized labels.
         */
        public final int compare(Object o1, Object o2) {
            LabelValue lhs = (LabelValue) o1;
            LabelValue rhs = (LabelValue) o2;

            return c.compare(lhs.getLabel(), rhs.getLabel());
        }
    }
}
