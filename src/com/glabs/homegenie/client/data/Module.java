/*
    This file is part of HomeGenie for Adnroid.

    HomeGenie for Adnroid is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HomeGenie for Adnroid is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with HomeGenie for Adnroid.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 *     Author: Generoso Martello <gene@homegenie.it>
 */

package com.glabs.homegenie.client.data;

import com.glabs.homegenie.client.Control;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class Module {

    public enum DeviceTypes {
        Generic,
        Sensor,
        Dimmer,
        Light,
        Switch,
        Temperature,
        DoorWindow,
        Program,
        Thermostat, Fan, Shutter, Siren
    }

    public String Domain;
    public String Address;
    public String Name;
    public String Description;
    public String DeviceType;
    public ArrayList<ModuleParameter> Properties = new ArrayList<ModuleParameter>();
    
    public String RoutingNode;

    // ViewModel
    public Object Adapter;
    public android.view.View View;

    public String getDisplayName() {
        return this.Name;
    }

    public String getDisplayAddress() {
        String domain = this.Domain;
        if (domain.indexOf('.') > 0) {
            domain = domain.substring(domain.lastIndexOf('.') + 1);
        }
        return domain + " " + this.DeviceType + " " + this.Address;
    }

    public static double getDoubleValue(String propvalue) {
        double doubleval = 0;
        try {
            doubleval = Double.parseDouble(propvalue);
        } catch (Exception e) {
        }
        return doubleval;
    }

    public static String getDisplayLevel(String level) {
        String retlevel = "";
        if (level != null && !level.equals("")) {
            double md = getDoubleValue(level);
            if (md > 0.9) {
                retlevel = "ON";
            } else if (md > 0) {
                retlevel = Math.round(md * 100) + "%";
            } else {
                retlevel = "OFF";
            }
        }
        return retlevel;
    }

    public static String getFormattedNumber(String propvalue) {
        DecimalFormat decimalFormatter = new DecimalFormat();
        decimalFormatter.setMaximumFractionDigits(2);
        String formattedval = "";
        if (propvalue != null && !propvalue.equals("")) {
            try {
                formattedval = decimalFormatter.format(Double.parseDouble(propvalue));
            } catch (Exception e) {
            }
        }
        return formattedval;
    }


    public ModuleParameter getParameter(String pname) {
        ModuleParameter retValue = null;
        for (ModuleParameter mp : this.Properties) {
            if (mp.Name.equals(pname)) {
                retValue = mp;
                break;
            }
        }
        return retValue;
    }


    public void setParameter(String name, String v) {
        ModuleParameter mp = getParameter(name);
        if (mp != null) {
            mp.Value = v;
        } else {
            mp = new ModuleParameter(name, v);
            Properties.add(mp);
        }
    }

    public void setParameter(String name, String v, Date timestamp) {
        ModuleParameter mp = getParameter(name);
        if (mp != null) {
            mp.Value = v;
            mp.UpdateTime = timestamp;
        } else {
            mp = new ModuleParameter(name, v);
            Properties.add(mp);
        }
    }

    public void control(String apicommand, final Control.ServiceCallCallback callback) {
        Control.callServiceApi(this.Domain + "/" + this.Address + "/" + apicommand, new Control.ServiceCallCallback() {
            @Override
            public void serviceCallCompleted(String response) {
                if (callback != null) callback.serviceCallCompleted(response);
            }
        });
    }

}
