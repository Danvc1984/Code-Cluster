
close all;
clear all;

I=imread('obj2_.png');
figure(1),imshow(I);
gris= rgb2gray(I);

n=imhist(gris); 
N=sum(n);
max=0;

%Vector para guardar varianzas
varia = [];

for i=1:256
     %Probabilidad para cada nivel de intensidad
     P(i)=n(i)/N; 
end

for k = 1:255     

    %Probabilidad para la class 1
    w0=sum(P(1:k)); 
    %Probabilidad para la class 2
    w1=sum(P(k+1:256)); 
    
    % media clase 1
    u0=dot([0:k-1],P(1:k))/w0; 
    % media clase 2
    u1=dot([k:255],P(k+1:256))/w1; 
    
    vsum = 0;
    %varianza clase 1
    for i = 1:k
     vsum = vsum + ((i-u0)^2)*(n(i));
    end
    var0 =  vsum/w0;
    
    vsum = 0;
    %varianza clase 2
    for i = k+1:256
     vsum = vsum + ((i-u1)^2)*(n(i));
    end
    var1 =  vsum/w1;
    
    %varianza entre clases
    sigma = w0*var0+w1*var1;
   
    %Calcula el complemento de la varianza 
    %sigma=w0*w1*((u1-u0)^2); 
    
    varia(k)= sigma;
    
    % el punto de segmentacion se da por la maxima varianza entre las 
    %clases  
%     if sigma >= max; 
%         max=sigma; 
%         umbral=k; 
%     end
end

% el punto de segmentacion se da donde empieza a subir x.
for x = 1:256
    if (varia(x)<varia(x+1))
    umbral = x;
    break;
    end
end

%Muestra el histograma con el umbral resaltado
figure(2),imhist(gris); 
hold on;
line([umbral, umbral], ylim, 'LineWidth', 2 ,'Color',[0.4 0.1 0.5]);

%Muestra Grafica de varianza
figure(3),plot(varia);
hold on;
line([umbral, umbral], ylim, 'LineWidth', 2 ,'Color',[0.4 0.1 0.5]);

%Muestra resultado en imagen binaria
imgsegmentada=im2bw(gris,umbral/255); 
figure(4),imshow(imgsegmentada); 

% Metodo de matlab
% level = graythresh(I);
% otsuML = im2bw(level);
% figure(5),imshow(bw);
