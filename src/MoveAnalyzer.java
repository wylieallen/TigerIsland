import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Wylie on 3/26/2017.
 */
public class MoveAnalyzer
{
    private Board board;
    private ArrayList<HexButton> overallMoveset;
    private ArrayList<HexButton> legalMoves;
    private ArrayList<TilePlacementMove> legalTilePlacements;
    private ArrayList<BuildingPlacementMove> legalTotoroPlacements;
    private ArrayList<BuildingPlacementMove> legalVillagerPlacements;
    private ArrayList<BuildingPlacementMove> legalTigerPlacements;
    private ArrayList<SettlementExpansionMove> legalSettlementExpansions;

    public MoveAnalyzer(Board board)
    {
        this.board = board;
        updateMoveset();
        updateTilePlacements();
        updateBuildingPlacements();
        updateSettlementExpansions();
    }

    public void analyze()
    {
        updateMoveset();
        updateTilePlacements();
        updateBuildingPlacements();
        updateSettlementExpansions();
    }

    public void updateMoveset()
    {
        overallMoveset = new ArrayList<HexButton>(board.getButtonMap().values());
    }

    public void updateLegalMoves()
    {
        legalMoves = new ArrayList<HexButton>();
    }

    public void updateTilePlacements()
    {
        legalTilePlacements = new ArrayList<TilePlacementMove>();
        Tile activeTile = board.getDeck().getTopTile();
        for(HexButton hex : overallMoveset)
        {
            for(int i = 0; i < 6; i++)
            {
                activeTile.setOrientation(Orientation.values()[i]);
                if(board.tilePlacementIsLegal(activeTile, hex))
                {
                    legalTilePlacements.add(new TilePlacementMove(board.getActivePlayer(), hex.getOrigin(), activeTile.getOrientation()));
                }
            }
        }
        activeTile.setOrientation(board.getDeck().getOrientation());
    }

    public ArrayList<TilePlacementMove> getTilePlacements()
    {
        return legalTilePlacements;
    }

    public ArrayList<BuildingPlacementMove> getLegalVillagerPlacements()
    {
        return legalVillagerPlacements;
    }

    public ArrayList<BuildingPlacementMove> getLegalTigerPlacements()
    {
        return legalTigerPlacements;
    }

    public ArrayList<BuildingPlacementMove> getLegalTotoroPlacements()
    {
        return legalTotoroPlacements;
    }

    public ArrayList<SettlementExpansionMove> getLegalSettlementExpansions()
    {
        return legalSettlementExpansions;
    }

    public void updateBuildingPlacements()
    {
        legalVillagerPlacements = new ArrayList<BuildingPlacementMove>();
        legalTotoroPlacements = new ArrayList<BuildingPlacementMove>();
        legalTigerPlacements = new ArrayList<BuildingPlacementMove>();

        for(HexButton hex : overallMoveset)
        {
            if(board.buildingPlacementIsLegal(Building.VILLAGER, hex))
            {
                legalVillagerPlacements.add(new BuildingPlacementMove(board.getActivePlayer(), hex.getOrigin(), Building.VILLAGER));
            }

            if(board.buildingPlacementIsLegal(Building.TIGER, hex))
            {
                legalTigerPlacements.add(new BuildingPlacementMove(board.getActivePlayer(), hex.getOrigin(), Building.TIGER));
            }

            if(board.buildingPlacementIsLegal(Building.TOTORO, hex))
            {
                legalTotoroPlacements.add(new BuildingPlacementMove(board.getActivePlayer(), hex.getOrigin(), Building.TOTORO));
            }
        }
    }

    public void updateSettlementExpansions()
    {
        legalSettlementExpansions = new ArrayList<SettlementExpansionMove>();

        CopyOnWriteArrayList<Settlement> settlements = board.getSettlements();
        for(Settlement settlement : settlements)
        {
            if(settlement.getOwner() == board.getActivePlayer())
            {
                for (int i = 0; i < 4; i++) {
                    Terrain terrain = Terrain.values()[i];
                    Expansion expansion = board.getSettlementManager().getExpansion(settlement, terrain);
                    if (board.settlementExpansionIsLegal(expansion)) {
                        legalSettlementExpansions.add(new SettlementExpansionMove(board.getActivePlayer(), settlement.getHexes().get(0).getOrigin(), terrain));
                    }
                }
            }
        }

    }

    public boolean noPossibleBuildActions()
    {
        int moveCount = legalVillagerPlacements.size() + legalTigerPlacements.size() + legalTotoroPlacements.size() + legalSettlementExpansions.size();
        return (moveCount == 0);
    }


}