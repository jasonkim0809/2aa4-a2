
package ca.mcmaster.se2aa4.island.teamXXX;


class PerimeterDimensions {

    public Integer[] perimeterEdgePositions = new Integer[4];

    public PerimeterDimensions(int highestPoint, int mostRightPoint, int mostLeftPoint, int lowestPoint) {

        perimeterEdgePositions[0] = highestPoint;    
        perimeterEdgePositions[1] = mostRightPoint;    
        perimeterEdgePositions[2] = mostLeftPoint;    
        perimeterEdgePositions[3] = lowestPoint;    


    }

}