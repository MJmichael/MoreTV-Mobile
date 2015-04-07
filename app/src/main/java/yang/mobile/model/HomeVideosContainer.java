
package yang.mobile.model;

import java.util.List;

public class HomeVideosContainer {
   	private String cacheDate;
   	private Position position;
   	private String status;

 	public String getCacheDate(){
		return this.cacheDate;
	}
	public void setCacheDate(String cacheDate){
		this.cacheDate = cacheDate;
	}
 	public Position getPosition(){
		return this.position;
	}
	public void setPosition(Position position){
		this.position = position;
	}
 	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	}

    public class Position{
        private String code;
        private String count;
        private String name;
        private List<VideoItemInfo> positionItems;
        private List positions;
        private String title;
        private String type;

        public String getCode(){
            return this.code;
        }
        public void setCode(String code){
            this.code = code;
        }
        public String getCount(){
            return this.count;
        }
        public void setCount(String count){
            this.count = count;
        }
        public String getName(){
            return this.name;
        }
        public void setName(String name){
            this.name = name;
        }
        public List getPositionItems(){
            return this.positionItems;
        }
        public void setPositionItems(List positionItems){
            this.positionItems = positionItems;
        }
        public List<VideoItemInfo> getPositions(){
            return this.positions;
        }
        public void setPositions(List positions){
            this.positions = positions;
        }
        public String getTitle(){
            return this.title;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getType(){
            return this.type;
        }
        public void setType(String type){
            this.type = type;
        }
    }

}
