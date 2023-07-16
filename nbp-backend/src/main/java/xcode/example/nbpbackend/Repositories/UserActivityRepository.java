package xcode.example.nbpbackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xcode.example.nbpbackend.models.UserActivity;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity,Long> {
}
