package dev.jpaperformanceoptimization.repository;

import dev.jpaperformanceoptimization.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
