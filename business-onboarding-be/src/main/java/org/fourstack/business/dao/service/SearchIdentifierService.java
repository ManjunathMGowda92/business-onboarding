package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.repository.SearchIdentifierRepository;
import org.fourstack.business.entity.SearchIdentifier;
import org.fourstack.business.enums.SearchIdentifierType;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.model.ContactNumber;
import org.fourstack.business.model.Institute;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.utils.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class SearchIdentifierService {
    private final SearchIdentifierRepository searchIdentifierRepository;
    private final EntityMapper entityMapper;

    public void createSearchIdentifiers(Institute institute) {
        List<ContactNumber> contactNumbers = new ArrayList<>();
        contactNumbers.add(institute.getPrimaryContact());
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getContactNumbers())) {
            contactNumbers.addAll(institute.getContactNumbers());
        }
        createSearchIdentifiersForContacts(contactNumbers, institute.getObjectId());

        Set<String> emails = new HashSet<>();
        emails.add(institute.getPrimaryEmail());
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getEmails())) {
            emails.addAll(institute.getEmails());
        }
        createSearchIdentifiersForEmails(emails, institute.getObjectId());
        createSearchIdentifier(institute.getObjectId(), SearchIdentifierType.NAME.name(), institute.getName());
    }

    private void createSearchIdentifiersForEmails(Set<String> emails, String objectId) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(emails)) {
            emails.forEach(email -> createSearchIdentifier(objectId, SearchIdentifierType.EMAIL.name(), email));
        }
    }

    public void createSearchIdentifiersForContacts(List<ContactNumber> contactNumbers, String objectId) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(contactNumbers)) {
            contactNumbers.forEach(contactNumber -> createSearchIdentifier(objectId,
                    SearchIdentifierType.CONTACT_NUMBER.name(), contactNumber.getPhoneNumber()));
        }
    }

    public void createSearchIdentifier(String objectId, String identifierType, String identifierValue) {
        SearchIdentifier identifier = entityMapper.constructSearchIdentifier(identifierType,
                identifierValue, objectId);
        String entityKey = KeyGenerationUtil.generateSearchIdentifierKey(identifierType, identifierValue);
        identifier.setKey(entityKey);
        searchIdentifierRepository.save(identifier);
    }
}
