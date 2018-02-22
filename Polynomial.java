import java.util.*;
//

//shit is possibly buggy as HELL !!!!!

//
public class Polynomial{

    public ArrayList<Float> Coefficient;
    
    public Polynomial()
    {
        Coefficient= new ArrayList<Float>();
    }
    
    public Polynomial(Float... a)
    {
        Coefficient = new ArrayList<Float>(Arrays.asList(a));
    }
    private void adjust()
    {
        while(Coefficient.size()>=1 && Math.abs(Coefficient.get(Coefficient.size()-1))<0.00001)
        {
            Coefficient.remove(Coefficient.size()-1);
        }
    }
    
    public int deg()
    {
        return Coefficient.size()-1;
    }
    
    public float value(Float x)
    {
        float val=0;
        float x_pow=1;
        for(int i = 0; i< Coefficient.size(); i++)
        {
            val+=(x_pow* Coefficient.get(i));
            x_pow*=x;
        }
        return val;
    }

    public void addConst(float a)
    {
        for(int i = 0; i< Coefficient.size(); i++)
        {
            Coefficient.set(i, Coefficient.get(i)+a);
        }
        adjust();
    }

    public void multiplyByConst(float a)
    {
        for(int i = 0; i< Coefficient.size(); i++)
        {
            Coefficient.set(i, Coefficient.get(i)*a);
        }
        adjust();
    }

    public void add(Polynomial P)
    {
        while(Coefficient.size()<P.Coefficient.size())
        {
            Coefficient.add(0.f);
        }
        for(int i = 0; i<Math.min(Coefficient.size(),P.Coefficient.size()); i++)
        {
            Coefficient.set(i, Coefficient.get(i)+P.Coefficient.get(i));
        }
        adjust();
    }
    
    public void subtract(Polynomial P)
    {
        while(Coefficient.size()<P.Coefficient.size())
        {
            Coefficient.add(0.f);
        }
        for(int i = 0; i<Math.min(Coefficient.size(),P.Coefficient.size()); i++)
        {
            Coefficient.set(i, Coefficient.get(i)-P.Coefficient.get(i));
        }
        adjust();
    }
    
    public Polynomial divideByPoly(Polynomial P)//divides by polynomial - returns just quotient
    {
        if(P.Coefficient.size()==0)//if dividing by zero polynomial - return polynomial of one NAN
        {
            System.out.println("ERROR: Dividng by zero polynomial");
            Polynomial q=new Polynomial();
            float NAN = 1.f/0.f;
            q.Coefficient.add(NAN);
            return q;
        }
        if(Coefficient.size()<P.Coefficient.size()){//if deg(P) > deg(this) return zero polynomial
            Polynomial q=new Polynomial();
            return q;
        }
        else{
            Polynomial q=new Polynomial();
            P.adjust();
            int nsiz;
            nsiz=Coefficient.size()-P.Coefficient.size()+1;
            while(q.Coefficient.size()<nsiz){
                q.Coefficient.add(0.f);
            }
            Polynomial r=this;
            while(r.Coefficient.size()>0 && r.Coefficient.size()>=P.Coefficient.size())
            {
                float t;
                int i;
                t=r.Coefficient.get(r.Coefficient.size()-1)/P.Coefficient.get(P.Coefficient.size()-1);
                i=r.Coefficient.size()-P.Coefficient.size();
                Polynomial d=P;
                q.Coefficient.set(i,t);
                d.multiplyByConst(t);
                r.subtract(d);
            }
            q.adjust();
            return q;
        }
    }

};
