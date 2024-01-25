package hwicode.schedule.auth.infra.jpa_repository;

import hwicode.schedule.auth.domain.OauthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthUserRepository extends JpaRepository<OauthUser, Long> {
}
