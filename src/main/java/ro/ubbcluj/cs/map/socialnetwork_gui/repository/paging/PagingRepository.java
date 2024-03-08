package ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Entity;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.Repository;

public interface PagingRepository <ID, E extends Entity<ID>> extends Repository<ID,E>{
    Page<E> findAll(Pageable pageable);
}
