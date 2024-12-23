package com.massivecraft.factions.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasntFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.store.MStore;

public class CmdFactionsCreate extends FactionsCommand
{
    // -------------------------------------------- //
    // CONSTRUCTEUR
    // -------------------------------------------- //

    public CmdFactionsCreate()
    {
        this.addAliases("create");

        this.addRequiredArg("name");

        this.addRequirements(ReqHasntFaction.get());
        this.addRequirements(ReqHasPerm.get(Perm.CREATE.node));
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform()
    {
        String newName = this.arg(0);

        String[] validCountries = {
    "Afghanistan","Albanie","Algérie","Angola","Argentine","Arménie","Australie","Autriche","Azerbaïdjan","Bahamas","Bahreïn","Bangladesh","Biélorussie","Belgique","Belize","Bénin","Bouthan","Bolivie","Bosnie-Herzégovine","Botswana","Brésil","Bulgarie","Burkina Faso","Cambodge","Cameroun","Canada","Cap-Vert","Chili","Chine","Chypre","Colombie","Corée du Nord","Corée du Sud","Costa Rica","Croatie","Cuba","Danemark","Djibouti","Dominique","Égypte","Émirats Arabes Unis","Équateur","Érythrée","Espagne","Estonie","Eswatini","États-Unis","Éthiopie","Fidji","Finlande","France","Gabon","Gambie","Géorgie","Ghana","Grèce","Guatemala","Guinée","Guinée-Bissao","Guinée équatoriale","Haïti","Honduras","Hongrie","Inde","Indonésie","Irak","Iran","Irlande","Islande","Israël","Italie","Jamaïque","Japon","Jordanie","Kazakhstan","Kenya","Kiribati","Koweït","Laos","Lesotho","Lettonie","Liban","Liberia","Libye","Lituanie","Luxembourg","Macédoine","Madagascar","Malaisie","Malawi","Maldives","Mali","Malte","Maroc","Maurice","Mauritanie","Mexique","Moldavie","Monaco","Mongolie","Monténégro","Mozambique","Namibie","Nauru","Népal","Nicaragua","Niger","Nigeria","Norvège","Nouvelle-Zélande","Oman","Ouganda","Pakistan","Palestine","Panama","Papouasie-Nouvelle-Guinée","Paraguay","Pays-Bas","Pérou","Philippines","Pologne","Portugal","Qatar","République centrafricaine","République dominicaine","République du Congo","République tchèque","République Démocratique du Congo","Roumanie","Rwanda","Sahara Occidental","Saint-Kitts-et-Nevis","Saint-Lucie","Saint-Marin","Saint-Vincent-et-les-Grenadines","Salvador","Samoa","Sao Tomé-et-Principe","Sénégal","Serbie","Seychelles","Sierra Leone","Singapour","Slovaquie","Slovénie","Îles Salomon","Somalie","Soudan","Soudan du Sud","Sri Lanka","Suède","Suisse","Suriname","Syrie","Tadjikistan","Tanzanie","Tchad","Thaïlande","Timor oriental","Togo","Trinité-et-Tobago","Tunisie","Turkménistan","Turquie","Tuvalu","Uganda","Uruguay","Vanuatu",,"Venezuela","Vietnam","Yémen","Zambie","Zimbabwe"
        };

        boolean isValidCountry = Arrays.asList(validCountries).contains(newName);
        if (!isValidCountry)
        {
            msg("<b>Le nom doit être celui d'un pays existant. Veuillez réessayer.");
            return;
        }

        if (FactionColl.get().isNameTaken(newName))
        {
            msg("<b>Ce pays est déjà pris.");
            return;
        }

        ArrayList<String> nameValidationErrors = FactionColl.get().validateName(newName);
        if (nameValidationErrors.size() > 0)
        {
            sendMessage(nameValidationErrors);
            return;
        }

        String factionId = MStore.createId();

        EventFactionsCreate createEvent = new EventFactionsCreate(sender, factionId, newName);
        createEvent.run();
        if (createEvent.isCancelled()) return;

        Faction faction = FactionColl.get().create(factionId);
        faction.setName(newName);
        
        msender.setRole(Rel.LEADER);
        msender.setFaction(faction);
        
        EventFactionsMembershipChange joinEvent = new EventFactionsMembershipChange(sender, msender, faction, MembershipChangeReason.CREATE);
        joinEvent.run();

        msg("<i>Félicitations ! Vous avez créé la faction %s", faction.getName(msender));
        msg("<i>Vous devriez maintenant : %s", Factions.get().getOuterCmdFactions().cmdFactionsDescription.getUseageTemplate());

        if (MConf.get().logFactionCreate)
        {
            Factions.get().log(msender.getName()+" a créé le pays "+newName);
        }
    }
}
