package cz.muni.fi.valansky.perun.modules;


import cz.metacentrum.perun.core.api.Attribute;
import cz.metacentrum.perun.core.api.AttributeDefinition;
import cz.metacentrum.perun.core.api.AttributesManager;
import cz.metacentrum.perun.core.api.User;
import cz.metacentrum.perun.core.api.exceptions.AttributeNotExistsException;
import cz.metacentrum.perun.core.api.exceptions.InternalErrorException;
import cz.metacentrum.perun.core.api.exceptions.WrongAttributeAssignmentException;
import cz.metacentrum.perun.core.impl.PerunSessionImpl;
import cz.metacentrum.perun.core.implApi.modules.attributes.UserVirtualAttributesModuleAbstract;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Virtual attribute module to calculate user's age.
 *
 * @author Daniel Valanský
 */
public class urn_perun_user_attribute_def_virt_userAge extends UserVirtualAttributesModuleAbstract {

    @Override
    public Attribute getAttributeValue(PerunSessionImpl perunSession, User user, AttributeDefinition attributeDefinition) {
        Attribute attribute = new Attribute(attributeDefinition);

        try {
            Attribute source = perunSession.getPerunBl().getAttributesManagerBl().getAttribute(perunSession, user, "birthDay");
            LocalDate birthday = LocalDate.parse(source.valueAsString());

            int age = Period.between(birthday, LocalDate.now()).getYears();
            attribute.setValue(Integer.toString(age));
            return attribute;

        } catch (AttributeNotExistsException | WrongAttributeAssignmentException | DateTimeParseException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<String> getStrongDependencies() {
        List<String> dependencies = new ArrayList<>();
        dependencies.add("birthDay");
        return dependencies;
    }

    @Override
    public AttributeDefinition getAttributeDefinition() {
        AttributeDefinition attr = new AttributeDefinition();
        attr.setNamespace(AttributesManager.NS_USER_ATTR_VIRT);
        attr.setFriendlyName("userAge");
        attr.setDisplayName("UserAge");
        attr.setType(String.class.getName());
        attr.setDescription("User's age.");
        return attr;
    }
}
