
function AGB
    clear all;
    close all;
    
    maxk = 20;      %% No. m�ximo de iteraciones
    maximo = 1;     %% Obtener m�ximo (1) o m�nimo (0)
    
    %%Parametros funcion objetivo
    
    d = 8;                                          %% No. de dimensiones (objetos)
    peso_max = 50;                                  %% Peso m�ximo de la mochila
    valor = [60 100 120 30 10 200 20 40];           %% Valores de los objetos
    peso = [10 20 30 10 30 15 5 10];                %% Pesos de los objetos
    
    %% Par�metros:
    Np = 5;                 %% Tama�o de poblaci�n
    mutacion = 0.2;             %% Proporci�n de mutaci�n
    cruza = 0.9;                   %%proporcion de cruz
    k = 0;                            %%iteracion actual
    
    %% Inicializar 
    R = randi([0 1], Np, d);
    
    %% Evaluar poblaci�n
    for i = 1:Np
        fit(i, 1) = fnKnapsack(R(i, :), valor, peso, d, peso_max, maximo);
    end
    
    [f, ind] = sort(fit);
    R = R(ind, :);
    minf(k + 1) = f(1);
    
    %% Iteraciones:
    while k < maxk
        k = k + 1;  
        
        %% Selecci�n de padres para cruza (M�todo de la ruleta):
        E = sum(f);         %ecn 2.4
        E = f ./ E;         %ecn 2.5
        E = flip(E);
        q = [];
        q = cumsum(E);    
        for c = 1:Np
           padre1(c) = RULETA(q);
           padre2(c) = RULETA(q);
        end
        
        %% Cruza de un punto:
        H = [];        %hijos
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
            H(c, ind) = ~H(c, ind);  
        end
        
        for i = 1:sizeH
            Hf(i, 1) = fnKnapsack(H(i, :), valor, peso, d, peso_max, maximo);
        end
        
        
        %% Selecci�n:
        R = [R; H];
        f = [f; Hf];
        [f, ind] = sort(f);
        R = R(ind, :);
        minf(k) = f(1);
        
        f = f(1:Np, 1);           %restaurar fitness
        R = R((1: Np), :);          %restaurar indivuduos
        
        %%mostrar 
        if maximo == 1
            disp(sprintf('Generaci�n %d, fitness: %.2f', k, f(1)*-1));
        else
            disp(sprintf('Generaci�n %d, fitness: %.2f', k, f(1)));
        end
        
        for i = 1:d
            disp(sprintf('Posici�n de x%d: %.2f', i, R(1,i)));
        end
    end
    
    figure
    plot(minf)  %% Convergencia
end