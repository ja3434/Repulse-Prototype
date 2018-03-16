import java.util.*;
//

//gives you precise roots and all
// but haven't tested if those are all the roots there are...

//
public class Polynomial{

    public ArrayList<Double> Coefficients;
    
    public Polynomial()
    {
        Coefficients= new ArrayList<Double>();
    }
    
    public Polynomial(Double... a)
    {
        Coefficients = new ArrayList<Double>(Arrays.asList(a));
    }
    private void adjust()
    {
        while(Coefficients.size()>=1 && Math.abs(Coefficients.get(Coefficients.size()-1))<0.0001)
        {
            Coefficients.remove(Coefficients.size()-1);
        }
    }
    public void addCoefficient(Double a)
    {
        Coefficients.add(a);
    }
    public Double getCoefficient(int k) 
    {
        if(k>deg())
            throw new ArrayIndexOutOfBoundsException("za duży indeks, frajerze");
        return Coefficients.get(k);
    }
    public void setCoefficient(int k,Double a)
    {
        if(k>deg())
            throw new ArrayIndexOutOfBoundsException("za duży indeks, frajerze");
        Coefficients.set(k,a);
    }
    public void clear()
    {
        Coefficients.clear();
    }
    
    
    public int deg()
    {
        return Coefficients.size()-1;
    }
    
    public Double value(Double x)
    {
        adjust();
        Double val=0.0;
        Double x_pow=1.0;
        for(int i = 0; i< Coefficients.size(); i++)
        {
            val+=(x_pow* Coefficients.get(i));
            x_pow*=x;
        }
        return val;
    }

    public void addConst(Double a)
    {
        adjust();
        for(int i = 0; i< Coefficients.size(); i++)
        {
            Coefficients.set(i, Coefficients.get(i)+a);
        }
        adjust();
    }

    public void multiplyByConst(Double a)
    {
        adjust();
        for(int i = 0; i< Coefficients.size(); i++)
        {
            Coefficients.set(i, Coefficients.get(i)*a);
        }
        adjust();
    }

    public void add(Polynomial P)
    {
        adjust();
        while(Coefficients.size()<P.Coefficients.size())
        {
            Coefficients.add((Double)0.0);
        }
        for(int i = 0; i<Math.min(Coefficients.size(),P.Coefficients.size()); i++)
        {
            Coefficients.set(i, Coefficients.get(i)+P.Coefficients.get(i));
        }
        adjust();
    }
    
    public void subtract(Polynomial P)
    {
        adjust();
        while(Coefficients.size()<P.Coefficients.size())
        {
            Coefficients.add((Double)0.0);
        }
        for(int i = 0; i<Math.min(Coefficients.size(),P.Coefficients.size()); i++)
        {
            Coefficients.set(i, Coefficients.get(i)-P.Coefficients.get(i));
        }
        adjust();
    }
    public Polynomial derivative()
    {
        adjust();
        Polynomial q=new Polynomial();
        for(int i=1;i<=deg();i++)
        {
            q.addCoefficient((i*Coefficients.get(i)));
        }
        q.adjust();
        return q;
    }
    public Double[] roots()
    {
        adjust();
        
        int NumberOfIterations=50;//probably optimal ~ 30
        
        if((deg())<=0){
            return new Double[0];
        }
        Polynomial q=derivative();
        Double[] DerivRoots = q.roots();
        if(DerivRoots.length==0){//if  monotonic
            Double p=-1.0,r=1.0;
            for(int i=0;i<((NumberOfIterations*2)/3) && Math.signum(value(p))==Math.signum(value(r)); i++)
            //searching for p and r such that there is x in <p,r> and W(x) = 0
            {
                r*=2;
                p*=2;
            }
            if(Math.signum(value(p))==Math.signum(value(r))){//if there werent such x
                return new Double[0];
            }
            Double[] ret=new Double[1];
            for(int i=0;i<NumberOfIterations;i++)//binsearch for x
            {
                Double x=(p+r)/2;
                if(Math.signum(value(p))==Math.signum(value(x)))
                    p=x;
                else
                    r=x;
            }
            
            ret[0]=(p+r)/2;
            return ret;
        }
        //find additional points on edges such that they are on the other side of zero than fist and last derivative root
        Double p=-1.0,r=1.0;
        //searching for p and r such that there is x in <p , DRoots[0]> and W(x) = 0
        for(int i=0;i<((NumberOfIterations*2)/3) &&Math.signum(value(DerivRoots[0]))==Math.signum(value(DerivRoots[0]+p));i++)
        {
            p*=2;
        }
        //searching for p and r such that there is x in <DRoots[last] , r> and W(x) = 0
        for(int i=0;i<((NumberOfIterations*2)/3) &&Math.signum(value(DerivRoots[DerivRoots.length-1]))==Math.signum(value(DerivRoots[DerivRoots.length-1]+r));i++)
        {
            r*=2;
        }
        //System.out.printf("p %f r %f\n",DerivRoots[0]+p,DerivRoots[DerivRoots.length-1]+r);
        
        ArrayList<Double> IntrPoints=new ArrayList<Double>();// points of interest - if there are roots they are between them
        if(Math.signum(value(DerivRoots[0]))!=Math.signum(value(DerivRoots[0]+p)))
        {
            IntrPoints.add(DerivRoots[0]+p);
        }
        for(int i=0;i< DerivRoots.length;i++)
        {
            IntrPoints.add(DerivRoots[i]);
        }
        if(Math.signum(value(DerivRoots[DerivRoots.length-1]))!=Math.signum(value(DerivRoots[DerivRoots.length-1]+r)))
        {
            IntrPoints.add(DerivRoots[DerivRoots.length-1]+r);
        }
        //
        //finding roots
        ArrayList<Double> Roots=new ArrayList<Double>();
        for(int i=0;i<IntrPoints.size()-1;i++)
        {
            p=IntrPoints.get(i);
            r=IntrPoints.get(i+1);//for each consecutive pair of points check for root
            if(Math.signum(value(p))!=Math.signum(value(r)))//search iff there is root
            {
                for(int j=0;j<NumberOfIterations;j++)//binsearch for root
                {
                    Double x=(p+r)/2;
                    if(Math.signum(value(p))==Math.signum(value(x)))
                        p=x;
                    else
                        r=x;
                }
                Roots.add((p+r)/2);
            }
        }
        /*
        for(int i=0;i<Roots.size();i++)
        {
            System.out.print(Roots.get(i));
            System.out.print(" ");
        }
        //*/
        
        //TODO remove repeats, check and round all
        //
        // probaly checking and rp removing is unnecessary bcause of 194th line
        //

        Double[] ans=Roots.toArray(new Double[0]);//
        return ans;
    }
    Double smallestRoot(Double b)
    {
        return smallestRoot(0.0,b);
    }
    Double smallestRoot(Double a,Double b)
    {
        Double[] Roots=roots();
        for(int i=0;i<Roots.length;i++)
        {
            if(Roots[i]>b)
                break;
            if(Roots[i]>a)
                return Roots[i];
        }
        return a-1.0;
    }
    
    @Deprecated //definitly NOT working
    public Polynomial divide(Polynomial P)//divides by polynomial - returns just quotient
    {
        P.adjust();
        adjust();
        if(P.Coefficients.size()==0)//if dividing by zero polynomial - return polynomial of one NAN
        {
            System.out.println("ERROR: Dividng by zero polynomial");
            Polynomial q=new Polynomial();
            Double NAN = (Double)1.0/(Double)0.0;
            q.Coefficients.add(NAN);
            return q;
        }
        if(Coefficients.size()<P.Coefficients.size()){//if deg(P) > deg(this) return zero polynomial
            Polynomial q=new Polynomial();
            return q;
        }
        else{
            Polynomial q=new Polynomial();
            P.adjust();
            int nsiz;
            nsiz=Coefficients.size()-P.Coefficients.size()+1;
            while(q.Coefficients.size()<nsiz){
                q.Coefficients.add((Double)0.0);
            }
            Polynomial r=this;
            while(r.Coefficients.size()>0 && r.Coefficients.size()>=P.Coefficients.size())
            {
                Double t;
                int i;
                t=r.Coefficients.get(r.Coefficients.size()-1)/P.Coefficients.get(P.Coefficients.size()-1);
                i=r.Coefficients.size()-P.Coefficients.size();
                Polynomial d=P;
                q.Coefficients.set(i,t);
                d.multiplyByConst(t);
                r.subtract(d);
            }
            q.adjust();
            return q;
        }
    }

};
