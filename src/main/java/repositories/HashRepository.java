package repositories;

import entity.Hash;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;


@Repository
public interface HashRepository extends JpaRepository<Hash,String> {

    Hash findByUrl (String url);

}
