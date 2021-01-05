package cz.muni.fi.valansky.perun.modules;

import cz.metacentrum.perun.core.api.Attribute;
import cz.metacentrum.perun.core.api.AttributeDefinition;
import cz.metacentrum.perun.core.api.AttributesManager;
import cz.metacentrum.perun.core.api.User;
import cz.metacentrum.perun.core.api.exceptions.WrongAttributeValueException;
import cz.metacentrum.perun.core.api.exceptions.WrongReferenceAttributeValueException;
import cz.metacentrum.perun.core.impl.PerunSessionImpl;
import cz.metacentrum.perun.core.implApi.modules.attributes.UserAttributesModuleAbstract;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Attribute module for user's birthday.
 *
 * @author Daniel Valanský
 */
public class urn_perun_user_attribute_def_def_birthday extends UserAttributesModuleAbstract {

    private static final Pattern dayPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    @Override
    public void checkAttributeSyntax(PerunSessionImpl perunSession, User user, Attribute attribute) throws WrongAttributeValueException {
        if (attribute.getValue() == null) return;

        if (!(attribute.getValue() instanceof String))
            throw new WrongAttributeValueException(attribute, user, "Wrong type of the attribute. Expected: String");

        Matcher matcher = dayPattern.matcher(attribute.valueAsString());

        if (!matcher.matches()) {
            throw new WrongAttributeValueException(attribute, user, "Birthday does not match YYYY-MM-DD format.");
        }

    }

    @Override
    public void checkAttributeSemantics(PerunSessionImpl perunSession, User user, Attribute attribute) throws WrongReferenceAttributeValueException {
        if (attribute.getValue() == null)
            throw new WrongReferenceAttributeValueException(attribute, null, user, null, "User attribute birthday cannot be null.");

        try {
            LocalDate.parse(attribute.valueAsString());
        } catch (DateTimeParseException e) {
            throw new WrongReferenceAttributeValueException(attribute, null, user, null, "Birthday is not valid.");
        }
    }

    @Override
    public AttributeDefinition getAttributeDefinition() {
        AttributeDefinition attr = new AttributeDefinition();
        attr.setNamespace(AttributesManager.NS_USER_ATTR_DEF);
        attr.setFriendlyName("birthDay");
        attr.setDisplayName("Birthday");
        attr.setType(String.class.getName());
        attr.setDescription("User's birthday.");
        return attr;
    }
}
