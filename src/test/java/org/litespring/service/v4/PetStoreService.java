package org.litespring.service.v4;

import org.junit.Test;
import org.litespring.beans.factory.annotation.Autowired;
import org.litespring.dao.v4.AccountDao;
import org.litespring.dao.v4.ItemDao;
import org.litespring.stereotype.Component;

import javax.xml.ws.ServiceMode;

/**
 * @objective :
 * @date :2019/11/15- 9:46
 */
@ServiceMode
@Component(value="petStore")
public class PetStoreService {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private ItemDao itemDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }
}
