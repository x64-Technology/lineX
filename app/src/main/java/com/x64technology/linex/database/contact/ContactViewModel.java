package com.x64technology.linex.database.contact;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.x64technology.linex.models.Contact;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    final private ContactRepo contactRepo;
    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactRepo = new ContactRepo(application);
    }

    public LiveData<List<Contact>> getContacts() {
        return contactRepo.getContacts();
    }

    public void insert(Contact contact) {
        contactRepo.insert(contact);
    }

    void update(Contact contact) {
        contactRepo.update(contact);
    }

    void delete(Contact contact) {
        contactRepo.delete(contact);
    }

    Contact getContactById(String id) {
        return contactRepo.getContactById(id);
    }

    Contact getContactByUserId(String userId) {
        return contactRepo.getContactByUserId(userId);
    }
}