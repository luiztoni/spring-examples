package br.luiztoni.batch.contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ContactProcessor implements ItemProcessor<Contact, Contact> {
    private static final Logger log = LoggerFactory.getLogger(ContactProcessor.class);
    @Override
    public Contact process(Contact contact) throws Exception {
        log.info("converting...");
        return new Contact(contact.getName().toUpperCase(), contact.getBirthday());
    }
}
