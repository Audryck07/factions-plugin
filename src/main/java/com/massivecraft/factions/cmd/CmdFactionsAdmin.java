package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsAdmin extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCTION
	// -------------------------------------------- //
	
	public CmdFactionsAdmin()
	{
		// Alias
		this.addAliases("admin");

		// Arguments
		this.addOptionalArg("on/off", "flip");
		
		// Conditions requises
		this.addRequirements(ReqHasPerm.get(Perm.ADMIN.node));
	}

	// -------------------------------------------- //
	// SURCHARGE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Arguments
		Boolean cible = this.arg(0, ARBoolean.get(), !msender.isUsingAdminMode());
		if (cible == null) return;
		
		// Appliquer
		msender.setUsingAdminMode(cible);		
		
		// Informer
		String desc = Txt.parse(msender.isUsingAdminMode() ? "<g>ACTIVÉ" : "<b>DÉSACTIVÉ");
		
		String messageVous = Txt.parse("<i>%s %s <i>le mode administrateur bypass.", msender.getDisplayName(msender), desc);
		String messageLog = Txt.parse("<i>%s %s <i>le mode administrateur bypass.", msender.getDisplayName(IdUtil.getConsole()), desc);
		
		msender.sendMessage(messageVous);
		Factions.get().log(messageLog);
	}
	
}
