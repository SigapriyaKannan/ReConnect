package com.dal.asdc.reconnect.repository;

import com.dal.asdc.reconnect.model.ReferralRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<ReferralRequests,Integer>
{

    @Query("SELECT r.referrer.userID FROM ReferralRequests r WHERE r.referent.userID = :referentId AND r.status = 'PENDING'")
    List<Integer> findReferrerIdsByReferentIdAndStatusPending(@Param("referentId") int referentId);


    @Query("SELECT r.referrer.userID FROM ReferralRequests r WHERE r.referent.userID = :referentId AND r.status = 'ACCEPTED'")
    List<Integer> findReferrerIdsByReferentIdAndStatusAccepted(@Param("referentId") int referentId);

    @Query("SELECT r.referent.userID FROM ReferralRequests r WHERE r.referrer.userID = :referrerID AND r.status = 'PENDING'")
    List<Integer> findReferentIdsByReferrerIdAndStatusPending(@Param("referrerID") int referrerID);
}