package com.nesmelov.findme.dao;


import com.nesmelov.findme.model.TomcatEntry;
import java.util.List;

/**
 *
 */
public interface TomcatDAO {

    void save(TomcatEntry entry);

    List<TomcatEntry> list();
}
