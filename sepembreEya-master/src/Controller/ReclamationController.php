<?php

namespace App\Controller;

use App\Entity\Reclamation;
use App\Form\ReclamationType;
use App\Form\ReponseType;
use App\Repository\ReclamationRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\JsonResponse;




#[Route('/reclamation')]
class ReclamationController extends AbstractController
{
    #[Route('/stats', name: 'app_reclamation_stat', methods: ['GET'])]
    public function statistics(ReclamationRepository $reclamationRepository)
    {
        $repository = $this->getDoctrine()->getRepository(Reclamation::class);

            $data = $repository->createQueryBuilder('r')
                ->select('r.etatReservation, COUNT(r.id) as count')
                ->groupBy('r.etatReservation')
                ->getQuery()
                ->getResult();

            return $this->render('reclamation/chart.html.twig', [
                'data' => $data,
            ]);
    }
    #[Route('/trieasc', name: 'app_trieasc', methods: ['GET'])]
public function ascendingAction(ReclamationRepository $reclamationRepository)
{
    return $this->render('reclamation/index.html.twig', [
        'reclamations' => $reclamationRepository->findAllAscending("r.date_Rec"),
    ]);
}


// #[Route('/aboutttt', name: 'aboutttt', methods: ['GET'])]
// public function about(): Response
// {
//     return $this->render('about.html.twig');
// }


#[Route('/triedesc', name: 'app_triedesc', methods: ['GET'])]
public function descendingAction(ReclamationRepository $reclamationRepository)
{
   
    return $this->render('reclamation/index.html.twig', [
        'reclamations' => $reclamationRepository->findAllDescending("r.date_Rec"),
    ]);

}

#[Route('/mesreclamation', name: 'mesreclamation', methods: ['GET'])]
public function mesreclamation(ReclamationRepository $reclamationRepository)
{
    return $this->render('reclamation/show.html copy.twig', [
        'reclamations' => $reclamationRepository->findAll(),
    ]);
}


    #[Route('/search', name: 'app_search_reclamation', methods: ['POST'])]
public function searchAction(Request $request, EntityManagerInterface $entityManager): Response
{
    $query = $request->request->get('query'); // Get the search query from the form input

    // Create a DQL query to filter reclamations based on recText
    $dql = "SELECT r FROM App\Entity\Reclamation r WHERE r.recText LIKE :query";
    
    // Execute the DQL query
    $reclamations = $entityManager->createQuery($dql)
        ->setParameter('query', '%' . $query . '%')
        ->getResult();

    // Render the 'index.html.twig' template with the filtered reclamations
    return $this->render('reclamation/index.html.twig', [
        'reclamations' => $reclamations,
    ]);
}
    #[Route('/', name: 'app_reclamation_index', methods: ['GET'])]
    public function index(ReclamationRepository $reclamationRepository): Response
    {
        return $this->render('reclamation/index.html.twig', [
            'reclamations' => $reclamationRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_reclamation_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $reclamation = new Reclamation();

        // Set the date_Rec field to the current date and time
        $reclamation->setDateRec(new \DateTime());

        $form = $this->createForm(ReclamationType::class, $reclamation);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $reclamation->setIdUser($this->getUser());
            $reclamation->setEtatReservation(false);
            $entityManager->persist($reclamation);
            $entityManager->flush();

            return $this->redirectToRoute('mesreclamation', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('reclamation/new.html.twig', [
            'reclamation' => $reclamation,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_reclamation_show', methods: ['GET'])]
    public function show(Reclamation $reclamation): Response
    {
        return $this->render('reclamation/show.html.twig', [
            'reclamation' => $reclamation,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_reclamation_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Reclamation $reclamation, EntityManagerInterface $entityManager): Response
    {
        if ($this->isGranted('ROLE_ADMIN') && $this->getUser()!= $reclamation->getIdUser()) {
            $form = $this->createForm(ReponseType::class, $reclamation);
            $form->handleRequest($request);
            if ($form->isSubmitted() && $form->isValid()) {
                $reclamation->setEtatReservation(true);
                $entityManager->persist($reclamation);
                $entityManager->flush();
                return $this->redirectToRoute('app_reclamation_index', [], Response::HTTP_SEE_OTHER);
            }
            return $this->renderForm('reclamation/reponse.html.twig', [
                'reclamation' => $reclamation,
                'form' => $form,
            ]);
        } 
        if($this->getUser() == $reclamation->getIdUser() && null != $reclamation->getReponse()){
            return new Response("<script>alert('You can\'t answer your own reclamation!');window.location.href='/reclamation';</script>");
        }
        $formu = $this->createForm(ReclamationType::class, $reclamation);
        $formu->handleRequest($request);

        if ($formu->isSubmitted() && $formu->isValid()) {
            $entityManager->persist($reclamation);

            $entityManager->flush();

            return $this->redirectToRoute('mesreclamation', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('reclamation/edit.html.twig', [
            'reclamation' => $reclamation,
            'form' => $formu,
        ]);
           

       
    }



    #[Route('/{id}', name: 'app_reclamation_delete', methods: ['POST'])]
    public function delete(Request $request, Reclamation $reclamation, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$reclamation->getId(), $request->request->get('_token'))) {
            $entityManager->remove($reclamation);
            $entityManager->flush();
        }
        $ref = $request->headers->get('referer');
        if(  $ref === "http://localhost:8000/reclamation/mesreclamation"){
             return $this->redirectToRoute('mesreclamation', [], Response::HTTP_SEE_OTHER);
            
        }
        return $this->redirectToRoute('app_reclamation_index', [], Response::HTTP_SEE_OTHER);
    }







}
