package com.bob.validator;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.mesg.Messages;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.mesg.MZul;

public class EmptyCKEditorValidator extends AbstractValidator {

	@Override
	public void validate(ValidationContext ctx) {
		String val = (String) ctx.getProperty().getValue();
		if (val == null || "".equals(val.trim())) {
			Clients.wrongValue(ctx.getBindContext().getComponent(), Messages.get(MZul.EMPTY_NOT_ALLOWED));
			addInvalidMessage(ctx, "");
		}
	}

}
