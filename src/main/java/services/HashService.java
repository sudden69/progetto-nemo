package services;

import entity.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.HashRepository;

import java.util.Optional;

// import org.springframework.transaction.annotation.Transactional;

@Service
public class HashService {
    @Autowired
    private HashRepository hashRepository;
    /*
    @Transactional
    */
    public void addUrl(Hash hash){
        hashRepository.save(hash);
    }

    public Hash findUrlByHash(String id){
        Optional<Hash> ret = hashRepository.findById(id);
        return ret.get();
    }

}
