package com.wallet.crypto.trustapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.web3j.abi.datatypes.generated.Uint16;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 27/01/2018.
 */

public class Ticket extends Token implements Parcelable
{
    public final TicketInfo ticketInfo;
    public final List<Integer> balanceArray;

    public Ticket(TicketInfo tokenInfo, List<Integer> balances) {
        super(tokenInfo, BigDecimal.ZERO);
        this.balanceArray = balances;
        this.ticketInfo = tokenInfo;
    }

    private Ticket(Parcel in) {
        super(in, true);
        //now read in ticket
        ticketInfo = in.readParcelable(TicketInfo.class.getClassLoader());
        Object[] readObjArray = in.readArray(Object.class.getClassLoader());
        balanceArray = new ArrayList<Integer>();
        for (Object o : readObjArray)
        {
            Integer val = (Integer)o;
            balanceArray.add(val);
        }
    }

    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(ticketInfo, flags);
        dest.writeArray(balanceArray.toArray());
    }

    public List<Uint16> parseIDList(String userList)
    {
        List<Uint16> idList = new ArrayList<>();

        try
        {
            String[] ids = userList.split(",");

            for (String id : ids)
            {
                //remove whitespace
                String trim = id.trim();
                Uint16 thisId = new Uint16(Integer.parseInt(trim));
                idList.add(thisId);
            }
        }
        catch (Exception e)
        {
            idList = null;
        }

        return idList;
    }

    public List<Integer> parseIndexList(String userList)
    {
        //read given indicies and convert into internal format, error checking to ensure
        List<Integer> idList = new ArrayList<>();

        try
        {
            String[] ids = userList.split(",");

            for (String id : ids)
            {
                //remove whitespace
                String trim = id.trim();
                Integer thisId = Integer.parseInt(trim);

                if (thisId > 0)
                {
                    //TODO: store all values as a map
                    //find index in balance array
                    boolean added = false;
                    for (int index = 0; index < balanceArray.size(); index++)
                    {
                        if (balanceArray.get(index) == thisId)
                        {
                            idList.add(index);
                            added = true;
                            break;
                        }
                    }
                    if (!added) {
                        idList = null;
                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            idList = null;
        }

        return idList;
    }
}
