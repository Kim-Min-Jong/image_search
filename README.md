# image_search
내일배움캠프 안드로이드 심화 개인과제 with mvvm 아키텍쳐 연습  

![img.png](img.png)  

++ 공식 문서에 있는 repository pattern도 함께 연습    

장점  
 - 데이터가 있는 여러 저장소(Local Data Source, Remote Data Source)를 추상화하여 중앙 집중 처리 방식을 구현할 수 있음  
 - ViewModel은 repository가 local에서 데이터를 받아오는 지, remote에서 데이터를 받아오는 지 출처를 모르게 됨  
 - 결국 제공해주는 데이터를 이용만 하면 되므로, ViewModel은 비즈니스 로직에만 집중 할 수 있음    
![img_1.png](img_1.png)
