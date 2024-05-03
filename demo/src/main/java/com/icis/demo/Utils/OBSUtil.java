package com.icis.demo.Utils;

import com.icis.demo.DAO.ObsDAO;
import com.icis.demo.Entity.ObsPerson;
import com.icis.demo.Entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OBSUtil {
    @Autowired
    private static ObsDAO obsDAO;

    @Autowired
    public OBSUtil(ObsDAO obsDAO) {
        this.obsDAO = obsDAO;
    }
    public ObsPerson isRealStudent(String email){
        ObsPerson person = obsDAO.findObsPersonByEmail(email);
        return person;
    }

    public ObsPerson isRealStaff(String email) {
        ObsPerson person = obsDAO.findObsPersonByEmail(email);
        return person;
    }
}
