package com.nesmelov.findme.service;

import com.nesmelov.findme.dao.JdbcTomcatDAO;
import com.nesmelov.findme.dao.TomcatDAO;
import com.nesmelov.findme.model.TomcatEntry;
import java.util.List;

/**
 *
 */
public class TomcatService {

    private TomcatDAO dao = new JdbcTomcatDAO();

    public void addEntry(TomcatEntry entry) {
        dao.save(entry);
    }

    public List<TomcatEntry> getAllEntries() {
        return dao.list();
    }
}
