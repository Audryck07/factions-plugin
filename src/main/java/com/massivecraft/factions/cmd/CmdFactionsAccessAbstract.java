package com.massivecraft.factions.cmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.massivecraft.factions.RelationParticipator;
import com.massivecraft.factions.TerritoryAccess;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSFormatHumanSpace;
import com.massivecraft.massivecore.util.Txt;


public abstract class CmdFactionsAccessAbstract extends FactionsCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public PS chunk;
	public TerritoryAccess ta;
	public Faction hostFaction;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsAccessAbstract()
	{
		// Requirements
		this.addRequirements(ReqIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		chunk = PS.valueOf(me.getLocation()).getChunk(true);
		ta = BoardColl.get().getTerritoryAccessAt(chunk);
		hostFaction = ta.getHostFaction();
		
		this.innerPerform();
	}
	
	public abstract void innerPerform();

	public void sendAccessInfo()
	{
		sendMessage(Txt.titleize("Accès à " + chunk.toString(PSFormatHumanSpace.get())));
		msg("<k>Faction Hôte : %s", hostFaction.describeTo(msender, true));
		msg("<k>Faction Hôte Autorisée : %s", ta.isHostFactionAllowed() ? Txt.parse("<lime>VRAI") : Txt.parse("<rose>FAUX"));
		msg("<k>Joueurs Autorisés : %s", describeRelationParticipators(ta.getGrantedMPlayers(), msender));
		msg("<k>Factions Autorisées : %s", describeRelationParticipators(ta.getGrantedFactions(), msender));

	}
	
	public static String describeRelationParticipators(Collection<? extends RelationParticipator> relationParticipators, RelationParticipator observer)
	{
		if (relationParticipators.size() == 0) return Txt.parse("<silver><em>none");
		List<String> descriptions = new ArrayList<String>();
		for (RelationParticipator relationParticipator : relationParticipators)
		{
			descriptions.add(relationParticipator.describeTo(observer));
		}
		return Txt.implodeCommaAnd(descriptions, Txt.parse("<i>, "), Txt.parse(" <i>and "));
	}
	
}
