package com.dal.asdc.reconnect.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dal.asdc.reconnect.dto.Request.Requests;
import com.dal.asdc.reconnect.enums.RequestStatus;
import com.dal.asdc.reconnect.model.ReferralRequests;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.RequestRepository;
import com.dal.asdc.reconnect.repository.UserDetailsRepository;
import com.dal.asdc.reconnect.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RequestServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @InjectMocks
    private RequestService requestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPendingRequestForReferent() {
        String sender = "test@example.com";
        Users user = new Users();
        user.setUserID(1);
        when(usersRepository.findByUserDetailsUserName(sender)).thenReturn(Optional.of(user));

        List<Integer> pendingRequestsID = Collections.singletonList(1);
        when(requestRepository.findReferrerIdsByReferentIdAndStatusPending(user.getUserID())).thenReturn(pendingRequestsID);

        Requests request = new Requests();
        when(userDetailsRepository.findRequestsByReferrerIds(pendingRequestsID)).thenReturn(Collections.singletonList(request));

        List<Requests> result = requestService.getPendingRequestForReferent(sender);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(request, result.get(0));
    }

    @Test
    public void testGetPendingRequestForReferrer() {
        String sender = "test@example.com";
        Users user = new Users();
        user.setUserID(1);
        when(usersRepository.findByUserDetailsUserName(sender)).thenReturn(Optional.of(user));

        List<Integer> pendingRequestsID = Collections.singletonList(1);
        when(requestRepository.findReferentIdsByReferrerIdAndStatusPending(user.getUserID())).thenReturn(pendingRequestsID);

        Requests request = new Requests();
        when(userDetailsRepository.findRequestsByReferrerIds(pendingRequestsID)).thenReturn(Collections.singletonList(request));

        List<Requests> result = requestService.getPendingRequestForReferrer(sender);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(request, result.get(0));
    }

    @Test
    public void testAcceptRequest() {
        String sender = "test@example.com";
        Users user = new Users();
        user.setUserID(1);
        when(usersRepository.findByUserDetailsUserName(sender)).thenReturn(Optional.of(user));

        int referentID = 1;
        when(requestRepository.updateStatusAndResponseDate(RequestStatus.ACCEPTED, LocalDateTime.now(), referentID, user.getUserID())).thenReturn(1);

        requestService.acceptRequest(sender, referentID);

    }

    @Test
    public void testRequestRejected() {
        String sender = "test@example.com";
        Users user = new Users();
        user.setUserID(1);
        when(usersRepository.findByUserDetailsUserName(sender)).thenReturn(Optional.of(user));
        int referentID = 1;
        when(requestRepository.updateStatusAndResponseDate(RequestStatus.REJECTED, LocalDateTime.now(), referentID, user.getUserID())).thenReturn(1); // Assuming update returns affected rows count

        requestService.requestRejected(sender, referentID);

    }

    @Test
    public void testGetAcceptedRequestForReferent() {

        int userId = 1;
        ReferralRequests request1 = new ReferralRequests();
        request1.setRequestId(1);
        ReferralRequests request2 = new ReferralRequests();
        request2.setRequestId(2);

        when(requestRepository.findByReferent_UserIDAndStatus(userId, RequestStatus.ACCEPTED))
                .thenReturn(List.of(request1, request2));

        List<ReferralRequests> result = requestService.getAcceptedRequestForReferent(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(request1.getRequestId(), result.get(0).getRequestId());
        assertEquals(request2.getRequestId(), result.get(1).getRequestId());
    }

    @Test
    public void testGetAcceptedRequestForReferent_NoRequests() {
        int userId = 1;

        when(requestRepository.findByReferent_UserIDAndStatus(userId, RequestStatus.ACCEPTED))
                .thenReturn(Collections.emptyList());

        List<ReferralRequests> result = requestService.getAcceptedRequestForReferent(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

