/*
 * Copyright (C) 2012 CyborgDev <cyborg@alta189.com>
 *
 * This file is part of CyborgREST
 *
 * CyborgREST is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CyborgREST is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alta189.cyborg.rest.core;

import com.alta189.cyborg.Cyborg;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.Set;

@Path("/cyborg")
public class CyborgProvider {

	private static final PeriodFormatter timeFormatter = new PeriodFormatterBuilder()
			.appendYears().appendSuffix(" years").appendSeparator(", ")
			.appendMonths().appendSuffix(" months").appendSeparator(", ")
			.appendWeeks().appendSuffix(" weeks").appendSeparator(", ")
			.appendDays().appendSuffix(" days").appendSeparator(", ")
			.appendHours().appendSuffix(" hours").appendSeparator(", ")
			.appendMinutes().appendSuffix(" minutes").appendSeparator(", ")
			.appendSeconds().appendSuffix(" seconds")
			.toFormatter();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CyborgInfo getCyborgInfo() {
		Cyborg cyborg = Cyborg.getInstance();
		CyborgInfo info = new CyborgInfo();
		info.setNick(cyborg.getNick());
		info.setIdent(cyborg.getIndent());
		info.setHostmask(cyborg.getHostmask());
		Set<Channel> channels = new HashSet<Channel>();
		for (org.pircbotx.Channel channel : cyborg.getChannels()) {
			if (!channel.getMode().contains("p") && !channel.getMode().contains("s")) {
				channels.add(new Channel(channel));
			}
		}
		info.setChannels(channels);
		info.setRunningTime(cyborg.getRunningTime());
		Period period = new Period(info.getRunningTime());
		info.setRunningTimeFormatted(timeFormatter.print(period));

		return info;
	}
}
