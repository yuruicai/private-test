package com.sinochem.yunlian.upm.sso.service;

import com.sinochem.yunlian.upm.sso.util.Constants;
import com.sinochem.yunlian.upm.sso.cache.SsoCacheFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by zhanghongze on 2015/11/19.
 */
@Service
public class TicketService {

    private static final String TICKET_KEY_PREFIX = "sso.ticket.";

    @Resource
    private SsoCacheFacade ssoCacheFacade;

    private String ticketSessionKey(String ticket){
        return TICKET_KEY_PREFIX + ticket;
    }

    public void saveTicket(String ticket, String token){
        ssoCacheFacade.set(ticketSessionKey(ticket), token, Constants.TICKET_TIMEOUT);
    }

    public void deleteTicket(String ticket){
        ssoCacheFacade.delete(ticketSessionKey(ticket));
    }

    public String getTicket(String ticket){
        return ssoCacheFacade.get(ticketSessionKey(ticket));
    }

    public static String generateTicket() {
        return "ST-" + UUID.randomUUID().toString().replaceAll("-", "");
    }
}
