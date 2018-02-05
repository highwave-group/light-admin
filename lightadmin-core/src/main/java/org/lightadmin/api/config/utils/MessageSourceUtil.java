package org.lightadmin.api.config.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceUtil {
	private static MessageSourceAccessor messages;

	@Autowired
	private void setMessageSource(MessageSource messageSource) {
		MessageSourceUtil.messages = new MessageSourceAccessor(messageSource);
	}

	public static MessageSourceAccessor messages() {
		return messages;
	}

	public static String getMessage(String code) {
		return messages.getMessage(code);
	}
}
