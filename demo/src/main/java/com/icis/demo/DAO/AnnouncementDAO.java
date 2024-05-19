package com.icis.demo.DAO;

import com.icis.demo.Entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "announcement")
public interface AnnouncementDAO extends JpaRepository<Announcement, Integer> {
}
