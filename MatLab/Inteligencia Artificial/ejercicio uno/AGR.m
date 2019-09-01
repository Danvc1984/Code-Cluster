%% ALGORITMO GENETICO REAL
function AGR
    clear all;
    close all;
    
    d = 2;          %% No. dimensiones
    l = 2;          %% L�mite inferior
    u = 32;         %% L�mite superior
    maxk = 20;      %% No. m�ximo de iteraciones
    maximo = 1;     %% Obtener m�ximo (1) o m�nimo (0)
    
    %% Par�metros:
    Np = 5;             %% Tama�o de poblaci�n
    mutacion = 0.2;     %% Proporci�n de mutaci�n
    cruza = 0.9;
    k = 0;                
    
    %% Inicializar 
    R = rand(Np, d) .* (u - l) + l;
    
    %% Evaluar poblaci�n
    for i = 1:Np
        %% Revaluar la posici�n y seg�n la posici�n x de la part�cula
        R(i, 2) = 50 - ((3/2) * R(i, 1));
        
        fit(i, 1) = fnAreaRegion(R(i, :), maximo);
    end
    
    [f, ind] = sort(fit);
    R = R(ind, :);
    minf(k + 1) = f(1);
    
    %% Iteraciones:
    while k < maxk
        k = k + 1;  
        
        %% Selecci�n de padres para cruza (M�todo de la ruleta):
        E = sum(f); 
        E = f ./ E;
        E = flip(E);
        q = [];
        q = cumsum(E);    
        for c = 1:Np
           padre1(c) = RULETA(q);
           padre2(c) = RULETA(q);
        end
        
        %% Cruza de un punto:
        H = [];
        Hf = [];
        for c = 1:Np
            hijo1 = [];
            hijo2 = [];
            
            if rand() <= cruza 
               if d == 2
                   hijo1 = [R(padre1(c), 1), R(padre2(c), 2)];
                   hijo2 = [R(padre1(c), 2), R(padre2(c), 1)];
               else
                   ptCruce = randi([1 d]);  %% Obtener el punto de cruce
                   hijo1 = [R(padre1(c), 1:ptCruce), R(padre2(c), ptCruce + 1:end)];
                   hijo2 = [R(padre2(c), 1:ptCruce), R(padre1(c), ptCruce + 1:end)];
               end
            end
            
            H = [H; hijo1; hijo2];
        end
       
        %% Mutaci�n:
        sizeH = size(H, 1); 
        Hm = rand(sizeH, d);
        Hm = Hm < mutacion;
        
        for c = 1:sizeH
            ind = find(Hm(c, :));
            H(c, ind) = rand(1, length(ind)) .* (u - l) + l;  
        end
        
        for i = 1:sizeH
            %% Revaluar la posici�n y seg�n la posici�n x de la part�cula
            H(i, 2) = 50 - ((3/2) * H(i, 1));
            
            Hf(i, 1) = fnAreaRegion(H(i, :), maximo);
        end
        
        R = [R; H];
        f = [f; Hf];
        [f, ind] = sort(f);
        R = R(ind, :);
        minf(k) = f(1);
        
        %% Selecci�n:
        f = f(1:Np, 1);           
        R = R((1: Np), :);
        
        if maximo == 1
            disp(sprintf('Generaci�n %d, fitness: %.2f', k, f(1)*-1));
        else
            disp(sprintf('Generaci�n %d, fitness: %.2f', k, f(1)));
        end
        
        disp(sprintf('Posici�n de x: %.2f', R(1,1)));
        disp(sprintf('Posici�n de y: %.2f', R(1,2)));
    end
    
    figure
    plot(minf)  %% Convergencia
end