package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.arg.ARMPlayer;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdFactionsDemote extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCTION
	// -------------------------------------------- //
	
	public CmdFactionsDemote()
	{
		// Alias
		this.addAliases("demote");

		// Arguments
		this.addRequiredArg("player");

		// Conditions requises
		this.addRequirements(ReqHasPerm.get(Perm.DEMOTE.node));
		
	}

	// -------------------------------------------- //
	// SURCHARGE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{	
		MPlayer cible = this.arg(0, ARMPlayer.getAny());
		if (cible == null) return;
		
		if (cible.getFaction() != msenderFaction)
		{
			msg("%s<b> n'est pas membre de votre pays.", cible.describeTo(msender, true));
			return;
		}
		
		if (cible == msender)
		{
			msg("<b>Le joueur cible ne peut pas être vous-même.");
			return;
		}

		if (cible.getRole() == Rel.MEMBER)
		{
			if (!msender.getRole().isAtLeast(Rel.OFFICER))
			{
				msg("<b>Vous devez être officier pour rétrograder un membre en recrue.");
				return;
			}
			cible.setRole(Rel.RECRUIT);
			msenderFaction.msg("%s<i> a été rétrogradé au rang de recrue dans votre pays.", cible.describeTo(msenderFaction, true));
		}
		else if (cible.getRole() == Rel.OFFICER)
		{
			if (!msender.getRole().isAtLeast(Rel.LEADER))
			{
				msg("<b>Vous devez être le chef pour rétrograder un officier en membre.");
				return;
			}
			cible.setRole(Rel.MEMBER);
			msenderFaction.msg("%s<i> a été rétrogradé au rang de membre dans votre pays.", cible.describeTo(msenderFaction, true));
		}
	}
	
}
