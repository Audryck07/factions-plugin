package com.massivecraft.factions.cmd;

import java.util.Map.Entry;

import com.massivecraft.factions.event.EventFactionsExpansions;
import com.massivecraft.factions.Perm;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsExpansions extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCTION
	// -------------------------------------------- //
	
	public CmdFactionsExpansions()
	{
		// Alias
		this.addAliases("e", "expansions");

		// Conditions requises
		this.addRequirements(ReqHasPerm.get(Perm.EXPANSIONS.node));
	}

	// -------------------------------------------- //
	// SURCHARGE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Événement
		EventFactionsExpansions event = new EventFactionsExpansions(sender);
		event.run();
		
		// Titre
		msg(Txt.titleize("Extensions de Factions"));
		
		// Lignes
		for (Entry<String, Boolean> entry : event.getExpansions().entrySet())
		{
			String name = entry.getKey();
			Boolean installed = entry.getValue();
			String format = (installed ? "<g>[X] <h>%s" : "<b>[ ] <h>%s");
			msg(format, name);
		}
		
		// Suggestion de lien
		msg("<i>Apprenez tout sur les expansions sur notre wiki :");
		msg("<aqua>https://wiki.redstonia.com");
	}
	
}
