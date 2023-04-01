package br.luiztoni.batch.contact;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContactFieldMapper implements FieldSetMapper<Contact> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Contact mapFieldSet(FieldSet fieldSet) throws BindException {
        String name = fieldSet.readString(0);
        String date = fieldSet.readString(1);
        Date birthday = null;
        try {
            birthday = dateFormat.parse(date);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        return new Contact(name, birthday);
    }
}
