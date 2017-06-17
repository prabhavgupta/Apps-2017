using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Interfaces
{
    public interface IGenericRepository<T> : IDisposable where T : class
    {
        IEnumerable<T> GetAll();
        T Get(object id);
        IEnumerable<T> Get(Expression<Func<T, Boolean>> predicate, Expression<Func<T, int>> orderBy, int skipCount, int takeCount, bool isAsc);
        IEnumerable<T> Get(Expression<Func<T, Boolean>> predicate, Expression<Func<T, double>> orderBy, int skipCount, int takeCount, bool isAsc);
        IEnumerable<T> Get(Expression<Func<T, Boolean>> predicate, Expression<Func<T, int>> orderBy, int skipCount, int takeCount);
        IEnumerable<T> Get(Expression<Func<T, Boolean>> predicate, Expression<Func<T, double>> orderBy, int skipCount, int takeCount);
        IEnumerable<T> Find(Expression<Func<T, Boolean>> predicate);
        double Sum(Expression<Func<T, Boolean>> wherePredicate, Expression<Func<T, double>> sumPredicate);
        int Sum(Expression<Func<T, Boolean>> wherePredicate, Expression<Func<T, int>> sumPredicate);
        double Average(Expression<Func<T, Boolean>> wherePredicate, Expression<Func<T, double>> averagePredicate);
        double Average(Expression<Func<T, Boolean>> wherePredicate, Expression<Func<T, int>> averagePredicate);
        double Max(Expression<Func<T, Boolean>> wherePredicate, Expression<Func<T, double>> maxPredicate);
        int Max(Expression<Func<T, Boolean>> wherePredicate, Expression<Func<T, int>> maxPredicate);
        void Create(T item);
        void Update(T item);
        void Delete(object id);
        void Dispose();
    }
}
